package br.com.restfulSmartFier.controller;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;

import br.com.restfulSmartFier.dao.AlarmeDAO;




public class Alarme {

	private static Alarme instancia = new Alarme();
	private Alarme() {

	}

	public static Alarme getInstancia() {
		return instancia;
	}
//
//	
	public void startAlarme(int segundos){
	long TEMPO = (1000 * segundos); // chama o método a cada 3 segundos 
	Timer timer = null; 
	if (timer == null) {
	            timer = new Timer();
	            TimerTask tarefa = new TimerTask() {
	                public void run() {
	                    try {
	                    	AlarmeDAO.getInstancia().getAlarme();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            };
	            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
	        }
	    }
	
	
}	
