package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.restfulSmartFier.controller.Alarme;

public class AppServletContextListener implements ServletContextListener{

	
	public static final String TINICIO = "TINICIO";
	
	@Override
	public void contextDestroyed(ServletContextEvent evt) {
//		Long tfinal = System.currentTimeMillis();
//		Long tinicio = (Long)evt.getServletContext().getAttribute(TINICIO);
//		
//		System.out.println("Tempo ativo "+ (tfinal - tinicio)/1000);
//		evt.getServletContext().removeAttribute(TINICIO);
	}

	@Override
	public void contextInitialized(ServletContextEvent evt) {
		System.out.println("PEGOUPORRAAAAAAAAAAAA");
		Alarme.getInstancia().startAlarme(60);
		evt.getServletContext().setAttribute(TINICIO, System.currentTimeMillis());
		
	}

	
	
}
