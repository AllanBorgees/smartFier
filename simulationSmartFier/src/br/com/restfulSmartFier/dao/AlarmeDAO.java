package br.com.restfulSmartFier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import br.com.restfulSmartFier.factory.ConexaoFactory;
import br.com.restfulSmartFier.factory.Email;
import br.com.restfulSmartFier.factory.Mqtt;
import br.com.restfulSmartFier.model.Estacao;

public class AlarmeDAO {
	
	Mqtt mqtt = Mqtt.getIntancia();
	
	private static AlarmeDAO instancia = new AlarmeDAO();

	private AlarmeDAO() {
	}

	public static AlarmeDAO getInstancia() {
		return instancia;
	}

	
	
	public void getAlarme() {
		FuncionarioDAO f = FuncionarioDAO.getInstancia();

		System.out.println("Entro no detAlarme alamerDAO");
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT estacao_id, nome_estacao, ip, modelo, descricao, endereco ");
		sql.append("FROM estacao ");

		try {
			Connection conexao = ConexaoFactory.conectar();
			PreparedStatement comando = conexao
					.prepareStatement(sql.toString());

			ResultSet resultado = comando.executeQuery();

			while (resultado.next()) {
				Estacao estacao = new Estacao();
				estacao.setEstacao_id(resultado.getString("estacao_id"));
				estacao.setNome_estacao(resultado.getString("nome_estacao"));
				estacao.setIp(resultado.getString("ip"));
				estacao.setModelo(resultado.getString("modelo"));
				estacao.setDescricao(resultado.getString("descricao"));
				estacao.setEndereco(resultado.getString("endereco"));
				estacao.setFuncionario(f.getFuncionarioEstacaoDAO(estacao.getEstacao_id()));
				
				
				 if (mqtt.isOnline(estacao.getEstacao_id())) {
					 System.out.println("Estacao online " + estacao.getEstacao_id());
					PreparedStatement update = conexao
							.prepareStatement("UPDATE alarme   SET  dtepoch = NULL , dtiso = NULL, contador = 0  WHERE estacao_id = ? ;");
					update.setString(1, estacao.getEstacao_id());
					update.executeUpdate();
					update.close();
				} else {

					Date d = new Date();
					long epochMaq = d.getTime();
					Long dtepoch = checkAlarme(estacao.getEstacao_id());
					
					if (dtepoch == null) {
						System.out.println("Primeira vez que ficou off " + estacao.getEstacao_id());
						PreparedStatement update = conexao
								.prepareStatement("UPDATE alarme SET  dtepoch = '"
										+ epochMaq
										+ "' , dtiso = '"
										+ d
										+ "' WHERE estacao_id = ? ;");
						update.setString(1, estacao.getEstacao_id());
						update.executeUpdate();
						update.close();

					} else {
						System.out.println("epochMaq " + epochMaq);
						System.out.println("dtepoch " + dtepoch);
						System.out.println("(epochMaq - dtepoch)"
								+ (epochMaq - dtepoch));
						System.out
								.println("(epochAlarme(estacao.getEstacao_id()) "
										+ (epochAlarme(estacao.getEstacao_id())));
						if ((epochMaq - dtepoch) >= epochAlarme(estacao
								.getEstacao_id())) {

							/*
							 * 
							 * Inserir metodo de chamar email
							 */

							System.out.println("Esta estacao ja ultrapassou o limite do TIMER "+estacao.getEstacao_id());
							
							new Email().enviarEmail(estacao);
							System.out.println(" EMAIL ENVIADO ");

							PreparedStatement update = conexao
									.prepareStatement("UPDATE alarme   SET  dtepoch = '"
											+ epochMaq
											+ "' , dtiso = '"
											+ d
											+ "' , contador = contador + 1  WHERE estacao_id = ? ;");
							update.setString(1, estacao.getEstacao_id());
							update.executeUpdate();
							update.close();

						}

					}
				}
			}
		} catch (Exception es) {
			es.printStackTrace();
		}
		System.out.println("acabou o metodo");
	}

	
	
	
	
	
	public Long checkAlarme(String estacaoId) {

		Long data = null;
		String retorno = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT dtepoch ");
		sql.append("FROM alarme ");
		sql.append("where estacao_id = ? ;");

		try {
			Connection conexao = ConexaoFactory.conectar();
			PreparedStatement comando = conexao
					.prepareStatement(sql.toString());

			comando.setString(1, estacaoId);

			ResultSet resultado = comando.executeQuery();

			while (resultado.next()) {
				retorno = resultado.getString("dtepoch");
			}
		} catch (Exception ex) {
			ex.getMessage();
		}

		if (retorno == null) {
			return data;
		}

		data = Long.parseLong(retorno);

		return data;
	}

	public Long epochAlarme(String estacaoId) {

		Long retorno = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT timer ");
		sql.append("FROM alarme ");
		sql.append("where estacao_id = ? ;");

		try {
			Connection conexao = ConexaoFactory.conectar();
			PreparedStatement comando = conexao
					.prepareStatement(sql.toString());
			comando.setString(1, estacaoId);
			ResultSet resultado = comando.executeQuery();

			while (resultado.next()) {
				retorno = Long.parseLong(resultado.getString("timer")) * 60000;
			}
		} catch (Exception ex) {
			ex.getMessage();
		}

		return retorno;
	}

	
	
	
	
	
	
	
	
	
	

	
	
	

//	public ArrayList<Estacao> getEstacoesDAO() {
//
//		ArrayList<Estacao> e = new ArrayList<Estacao>();
//
//		StringBuilder sql = new StringBuilder();
//
//		sql.append("SELECT estacao_id, nome_estacao, ip, modelo, descricao, endereco ");
//		sql.append("FROM estacao ");
//
//		try {
//			Connection conexao = ConexaoFactory.conectar();
//			PreparedStatement comando = conexao
//					.prepareStatement(sql.toString());
//
//			ResultSet resultado = comando.executeQuery();
//
//			while (resultado.next()) {
//				Estacao estacao = new Estacao();
//				estacao.setEstacao_id(resultado.getString("estacao_id"));
//				estacao.setNome_estacao(resultado.getString("nome_estacao"));
//				estacao.setIp(resultado.getString("ip"));
//				estacao.setModelo(resultado.getString("modelo"));
//				estacao.setDescricao(resultado.getString("descricao"));
//				estacao.setEndereco(resultado.getString("endereco"));
//
//				// if (mqtt.isOnline(estacao.getEstacao_id())) {
//				 if (false) {
////				if (true) {
//					estacao.setStatus("online");
//
//					PreparedStatement update = conexao
//							.prepareStatement("UPDATE alarme   SET  _dtepch = NULL , _dtiso = NULL, contador = 0  WHERE estacao_id = ? ;");
//					update.setString(1, estacao.getEstacao_id());
//					update.executeUpdate();
//					update.close();
//				} else {
//					estacao.setStatus("offline");
//
//					Date d = new Date();
//					long epochMaq = d.getTime();
//					Long dtepoch = checkAlarme(estacao.getEstacao_id());
//
//					if (dtepoch == null) {
//
//						PreparedStatement update = conexao
//								.prepareStatement("UPDATE alarme SET  _dtepch = '"
//										+ epochMaq
//										+ "' , _dtiso = '"
//										+ d
//										+ "' WHERE estacao_id = ? ;");
//						update.setString(1, estacao.getEstacao_id());
//						update.executeUpdate();
//						update.close();
//
//					} else {
//						System.out.println("epochMaq " + epochMaq);
//						System.out.println("dtepoch " + dtepoch);
//						System.out.println("(epochMaq - dtepoch)"
//								+ (epochMaq - dtepoch));
//						System.out
//								.println("(epochAlarme(estacao.getEstacao_id()) "
//										+ (epochAlarme(estacao.getEstacao_id())));
//						if ((epochMaq - dtepoch) >= epochAlarme(estacao
//								.getEstacao_id())) {
//
//							/*
//							 * 
//							 * Inserir metodo de chamar email
//							 */
//
//							System.out.println(" EMAIL ENVIADO ");
//
//							PreparedStatement update = conexao
//									.prepareStatement("UPDATE alarme   SET  _dtepch = '"
//											+ epochMaq
//											+ "' , _dtiso = '"
//											+ d
//											+ "' , contador = contador + 1  WHERE estacao_id = ? ;");
//							update.setString(1, estacao.getEstacao_id());
//							update.executeUpdate();
//							update.close();
//
//						}
//
//					}
//				}
//				e.add(estacao);
//			}
//		} catch (Exception es) {
//			es.printStackTrace();
//		}
//		System.out.println("acabou o metodo");
//		return e;
//	}


}
