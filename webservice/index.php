<?php

	ini_set('display_errors',true);
	error_reporting(E_ALL|E_STRICT);

	require_once("config.php");
	require_once("db.php");
	require_once("highscore.php");
	
	
	$database = new Database("score.sqlite");
	

	$currentPage = "score";
	if (array_key_exists("page", $_GET)) {
		if (strcmp($_GET['page'], "creator") == 0) {
			$currentPage = "creator";
		}	
	}


?>

<!DOCTYPE html>
<html lang="en">
  <head>
  	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Vodoji</title>
    <link rel="shortcut icon" href="ico/favicon.png">


    <link href="css/app.css" rel="stylesheet">
    
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
	<style>
		body { padding-top: 70px; }
	</style>
  </head>

  <body>

	    <!-- Fixed navbar -->
	    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
	      <div class="container">
	        <div class="navbar-header">
	          <a class="navbar-brand" href="#">VODOJI</a>
	        </div>
	        <div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li <?php if (strcmp($currentPage, "score") == 0) echo 'class="active"'; ?>><a href="?page=score">Highscore</a></li>
					<?php /*<li if (strcmp($currentPage, "creator") == 0) echo 'class="active"'; ><a href="?page=creator">Rhythm creator</a></li>*/ ?>
				</ul>
	        </div>
	      </div>
	    </div>
	    
	    <div class="container">
	    	<?php

	    		if (strcmp($currentPage, "creator") == 0) {
	    			require_once("view/creator_view.php");
	    		} else {
	    			require_once("view/highscore_view.php");
	    		}


	    	?>
		</div>

		
	</body>
</html>


