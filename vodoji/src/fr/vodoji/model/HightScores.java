package fr.vodoji.model;

import java.util.ArrayList;

public class HightScores {
	private String nom;
	private int score;
	
	private static ArrayList<HightScores> listHS = new ArrayList<HightScores>();
	

	public HightScores(){
	}
	
	/* Constructeur bidon =) */
	public HightScores(boolean ok){
		listHS.clear();
		new HightScores("J1",1500);
		new HightScores("J2",1499);
		new HightScores("J3",1350);
		new HightScores("J4",1349);
	}
	
	/* Créé un hight score et l'ajoute à la liste */
	public HightScores(String nom, int score){
		this.nom = nom;
		this.score = score;
		listHS.add(this);
	}
	
	public static String[] getListeHS() {
		String[] listString = new String[listHS.size()];
		for (int i = 0;i < listString.length;i++) {
			listString[i] = listHS.get(i).nom+" | "+listHS.get(i).score;
		}
		return listString;
	}
}
