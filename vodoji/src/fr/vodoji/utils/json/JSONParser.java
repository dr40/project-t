package fr.vodoji.utils.json;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class JSONParser {

	///////////////////////////////////////////////////////////////////////////////////////////
	// Protected enums/structures
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Type of token available
	 */
	protected enum TokenType {
		/**
		 * Simple word, format: ("_" | [a-z] | [A-Z]) + ("_" | [a-z] | [A-Z] | [0-9])*
		 */
		TOK_WORD,
		/**
		 * Symbol char.
		 */
		TOK_SYMBOL,
		/**
		 * Number, format: (0 + "." + [0-9]*) | [0-9]*
		 */
		TOK_NUMBER,
		/**
		 * String, format: "\"" + CHARS + "\""
		 * This parser respect the escaped sequence defined by JSON.
		 */
		TOK_STRING,
		/**
		 * End of File. Always the last token returned by nextToken.
		 */
		TOK_EOF
	}
	/**
	 * Structure representing a Token used by nextToken
	 */
	protected class Token {
		/**
		 *  Type of this token
		 */
		public TokenType type;
		/**
		 * Start position of this token (used mainly for nextToken)
		 */
		public int start;
		/**
		 * Length of this token (used mainly for nextToken)
		 */
		public int len;
		/**
		 * Value of this token
		 */
		public String value;
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// "Interface" - methods
	///////////////////////////////////////////////////////////////////////////////////////////

	public JSON parseString(String jsonCode) {
		JSON json = new JSON();
		if (doParse(json,jsonCode)) {
			return json;
		} else {
			return null;
		}
	}

	public JSON parseFile(String jsonFile) {
		return parseString(getFileContent(jsonFile));
	}

	public boolean parseStringToJSON(JSON json, String str) {
		return doParse(json, str);
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	// Parse
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Load JSON data from a string
	 * @param str String to load
	 * @param json JSON object fully loaded if no errors encountered
	 * @return true if loaded, false otherwise
	 */
	protected boolean doParse(JSON json, String str) {
        /* Initialize */
		json.clear();
		Token tok = new Token();
		tok.start = 0;
		tok.len = 0;
        /* Read the first '{' */
		tok = nextToken(str, tok);
		if (!isTokenEgalTo(tok, "{")) return false;
        /* Analyse each tokens */
		json.setType(JSONType.OBJECT);
		tok = nextToken(str, tok);
		privParseChild(json, str, tok);
		return true;
	}
	/**
	 * Load JSON data from a string
	 * @param obj JSON object to fill
	 * @param str String to parse
	 * @param tok Current token to analyse
	 * @return New token if parsed without errors (correct syntax, etc ...), null otherwise
	 */
	private Token privParse(JSON obj, String str, Token tok) {
        /* JSON: Read Name */
		if ((obj.getParent() == null) || (obj.getParent().getType() == JSONType.OBJECT)) {
            /* No parent Or Or Parent wait object: Wait child with name */
			if (tok.type != TokenType.TOK_STRING) return null;
			obj.setName(getTokenValue(tok));
            /* JSON: Read ':' */
			tok = nextToken(str, tok);
			if (!isTokenEgalTo(tok, ":")) return null;
            /* JSON: Read Value */
			tok = nextToken(str, tok);
		}
        /* JSON: Read Value */
		if (tok.type == TokenType.TOK_STRING) {
            /* No child: String value */
			obj.setType(JSONType.STRING);
			obj.setValue(getTokenValue(tok));
		} else if (tok.type == TokenType.TOK_NUMBER) {
            /* No child: Number value */
			obj.setType(JSONType.NUMBER);
			obj.setValue(getTokenValue(tok));
		} else if ((isTokenEgalTo(tok, "true")) || (isTokenEgalTo(tok, "false"))) {
            /* No child: Boolean value */
			obj.setType(JSONType.BOOLEAN);
			obj.setValue(getTokenValue(tok));
		} else if (isTokenEgalTo(tok, "{")) {
            /* Child */
			obj.setType(JSONType.OBJECT);
			tok = nextToken(str, tok);
		} else if (isTokenEgalTo(tok, "[")) {
            /* Array child */
			obj.setType(JSONType.ARRAY);
			tok = nextToken(str, tok);
		} else {
            /* EOF or Illegal Char: Error! */
			return null;
		}
        /* JSON: Read Child if have child */
		return (privParseChild(obj, str, tok));
	}
	/**
	 * Load JSON childs from a string
	 * @param obj JSON object to fill
	 * @param str String to parse
	 * @param tok Current token to analyse
	 * @return New token if parsed without errors (correct syntax, etc ...), null otherwise
	 */
	private Token privParseChild(JSON obj, String str, Token tok) {
        /* Parse child */
		if ((obj.getType() == JSONType.OBJECT) || (obj.getType() == JSONType.ARRAY)) {
			while (true) {
                /* Read Token */
				if ((isTokenEgalTo(tok, "}")) || (isTokenEgalTo(tok, "]"))) {
                    /* End of Child */
					break;
				}
                /* Add Child & Parse */
				JSON child = new JSON();
				obj.addChild(child);
				child.setParent(obj);
				tok = privParse(child, str, tok);
				if (tok == null) {
                    /* Error! */
					return null;
				}
                /* JSON Read: ',' | '}' | ']' */
				tok = nextToken(str, tok);
				if ((isTokenEgalTo(tok, "}")) || (isTokenEgalTo(tok, "]"))) {
                    /* End of Child */
					break;
				}
				if (!isTokenEgalTo(tok, ",")) return null;
				tok = nextToken(str, tok);
			}
		}
        /* Done: return token */
		return tok;
	}



	///////////////////////////////////////////////////////////////////////////////////////////
	// Protected - Token functions
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Retrieve the token just after a gived token.
	 * @param str String to parse
	 * @param fromToken Start after this token
	 * @return Token after fromToken
	 */
	protected Token nextToken(String str, Token fromToken) {
		return nextToken(str, fromToken.start + fromToken.len);
	}

	/**
	 * Retrieve token from a position.
	 * @param str String to parse
	 * @param fromPos Start position
	 * @return The token at fromPos
	 */
	protected Token nextToken(String str, int fromPos) {
        /* Initialize */
		int mode = 0; /* 0 = Wait Token Type, 1 = Wait String End, 2 = Wait Word End, 3 = Wait Number End, 4 = Comment multi-line, 5 = Comment single-line */
		int oldPos = 0;
		int numberDotFound = 0;
		Token tok = new Token();
		tok.type = TokenType.TOK_EOF;
		tok.start = str.length();
		tok.len = 0;
        /* Seek next tokens */
		for (int i = fromPos, max = str.length(); i <= max; i++) {
            /* Get char */
			char c = ((i < max) ? (str.charAt(i)) : ' ');
            /* Analyse char */
			if (mode == 0) {
                /* 0 - Wait Token Type */
				switch (c) {
					case ' ': case '\t': case '\n': case '\r':
                        /* WhiteSpace - ignore */
						break;
					case '\"':
                        /* String */
						oldPos = i;
						mode = 1;
						break;
						
					case '/':
						/* Check if multi-line or single-line */
						if ((i + 1 < max) && (str.charAt(i+1) == '*')) {
							/* Multi-line comment */
							oldPos = i;
							i++;
							mode = 4;
						} else if ((i + 1 < max) && (str.charAt(i+1) == '/')) {
							/* Single-line comment */
							oldPos = i;
							mode = 5;
						} else {
							/* Symbol */
							tok.type = TokenType.TOK_SYMBOL;
							tok.value = str.substring(i,i+1);
							tok.start = i;
							tok.len = 1;
							i = max + 1;
						}
						break;
					case '-':
                        /* Can be number or symbol */
						if ((i + 1 < max) && (str.charAt(i+1) >= '0') && (str.charAt(i+1) <= '9')) {
                            /* Number */
							oldPos = i;
							numberDotFound = 0;
							mode = 3;
						} else {
                            /* Done: Symbol */
							tok.type = TokenType.TOK_SYMBOL;
							tok.value = str.substring(i,i+1);
							tok.start = i;
							tok.len = 1;
							i = max + 1;
						}
						break;
					default:
						if ((c == '_') || ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
                            /* Word */
							oldPos = i;
							mode = 2;
						} else if ((c >= '0') && (c <= '9')) {
                            /* Number */
							oldPos = i;
							numberDotFound = 0;
							mode = 3;
						} else {
                            /* Done: Symbol */
							tok.type = TokenType.TOK_SYMBOL;
							tok.value = str.substring(i, i+1);
							tok.start = i;
							tok.len = 1;
							i = max + 1;
						}
						break;
				}
			} else if (mode == 1) {
                /* 1 - Wait String End */
				if (c == '\\') {
                    /* Ignore next */
					i++;
				} else if (c == '\"') {
                    /* Done: End of string found */
					tok.type = TokenType.TOK_STRING;
					tok.value = str.substring(oldPos, i+1);
					tok.start = oldPos;
					tok.len = i-oldPos+1;
					i = max + 1;
				}
			} else if (mode == 2) {
                /* 2 - Wait Word End */
				if ((c == '_') || ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9'))) {
                    /* Step */
				} else {
                    /* Done: End of word found */
					tok.type = TokenType.TOK_WORD;
					tok.value = str.substring(oldPos, i);
					tok.start = oldPos;
					tok.len = i - oldPos;
					i = max + 1;
				}
			} else if (mode == 3) {
                /* 3 - Wait Number End */
				if ((c == '.') && (numberDotFound == 0)) {
					numberDotFound++;
				} else if ((c >= '0') && (c <= '9')) {
                    /* Step */
				} else {
                    /* Done: End of word found */
					tok.type = TokenType.TOK_NUMBER;
					tok.value = str.substring(oldPos, i);
					tok.start = oldPos;
					tok.len = i - oldPos;
					i = max + 1;
				}
			} else if (mode == 4) {
				/* 4 - Wait End of comment multi-line */
				if ((c == '*') && (i + 1 < max) && (str.charAt(i+1) == '/')) {
					/* End of comment */
					i++;
					mode = 0;
				}
			} else if (mode == 5) {
				/* 4 - Wait End of comment single-line */
				if ((c == '\n') || (c == '\r')) {
					/* End of comment */
					mode = 0;
				}
			}
		}
        /* Return token */
		return tok;
	}

	/**
	 * Check if a token is egal to a value.
	 * @param tok Token to test
	 * @param value Value to test
	 * @return True if tok == value
	 */
	protected boolean isTokenEgalTo(Token tok, String value) {
		if ((value == null) || (tok.value == null)) return false;
		return (value.compareTo(tok.value) == 0);
	}

	/**
	 * Retrieve value of a token.
	 * @param tok Token considered
	 * @return Value of tok
	 */
	protected String getTokenValue(Token tok) {
		if (tok.type == TokenType.TOK_STRING) {
            /* Remove surround string char And interpret Escape Sequences */
			String tmp = tok.value.substring(1, tok.value.length() - 1);
			for(int i = 0, max = tmp.length(); i < max; i++) {
				if ((i + 1 < max) && (tmp.charAt(i) == '\\')) {
                    /* (cf. json.org :: Escape Sequences */
					switch (tmp.charAt(i+1)) {
						case '\"': case '\\': case '/':
							tmp = tmp.substring(0, i) + tmp.substring(i+1);
							max--;
							break;
						case 'b':
							tmp = tmp.substring(0, i) + "\b" + tmp.substring(i+2);
							max--;
							break;
						case 'f':
							tmp = tmp.substring(0, i) + "\f" + tmp.substring(i+2);
							max--;
							break;
						case 'n':
							tmp = tmp.substring(0, i) + "\n" + tmp.substring(i+2);
							max--;
							break;
						case 'r':
							tmp = tmp.substring(0, i) + "\r" + tmp.substring(i+2);
							max--;
							break;
						case 't':
							tmp = tmp.substring(0, i) + "\t" + tmp.substring(i+2);
							max--;
							break;
						case 'u':
                            /* Four-hex-digits: unicode char */
							// TODO: Implements Four-hex-digits for Escape Sequences full support
							break;
					}
				}
			}
            /* Return String value */
			return tmp;
		} else if (tok.type == TokenType.TOK_NUMBER) {
            /* Format number */
			// TODO: Add Format number
			return tok.value;
		} else {
			return tok.value;
		}
	}



	///////////////////////////////////////////////////////////////////////////////////////////
	// Tools - methods
	///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Retrieve file content
	 * @param file File path
	 * @return File content if no error, empty string ("") otherwise
	 */
	protected String getFileContent(String file) {
		/* Initialize */
		String result = "";
		File f = new File(file);
		BufferedInputStream bufIn = null;
		/* Retrieve file content */
		try {
	    	/* Initialize */
			FileInputStream fin = new FileInputStream(f);
			bufIn = new BufferedInputStream(fin);
			byte[] buf = new byte[1024];
			int bytesRead=0;
			while ((bytesRead = bufIn.read(buf)) != -1) {
				result += new String(buf, 0, bytesRead);
			}
		} catch (Exception e) {
	    	/*NOP*/;
		}
	    /* Close buffer */
		if (bufIn != null) {
			try {
				bufIn.close();
			} catch (Exception e) {
		    	/*NOP*/;
			}
		}
	    /* Return result */
		return result;
	}


}
