import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javax.annotation.ManagedBean;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author otavio
 */
@ManagedBean
@Named("circuitos")
@SessionScoped
public class Circuitos implements Serializable {

    private String circuito;
    private Map<String, String> circuitos;
    private ArrayList<chartData> listaTempos;

    @PostConstruct
    public void init() {
        circuitos = new HashMap<String, String>();
        getCircuitosFromBD();
       /* circuitos.put("AUSTRALIA", "AUSTRALIA");
        circuitos.put("Brazil", "Brazil");
        circuitos.put("Italy", "Italy");*/
    }

    public void getCircuitosFromBD() {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conexao = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/t2webdev", "root", "");
            stmt = (PreparedStatement) conexao.prepareStatement("SELECT `Circuito` FROM `tempos`");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nome=rs.getString("Circuito");
                if(!circuitos.containsValue(nome)){
                    circuitos.put(nome, nome);
                }
            }
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(Circuitos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("*-*--**********************Erro");
        }
        System.out.println("*-*--**********************Conectado!");
    }

    public void loadTempos(String s) throws SQLException {
        System.out.println(s);
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        listaTempos = new ArrayList<>();
        try {
            conexao = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/t2webdev", "root", "");
            stmt = (PreparedStatement) conexao.prepareStatement(s);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String tempo=rs.getString("tempo");
                String ano=rs.getString("ano");
                listaTempos.add(new chartData(ano, tempo));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Circuitos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("*-*--**********************Erro");
        } finally {
            createChart();
            rs.close();
            stmt.close();
            conexao.close();
        }
    }

    public void createChart(){
        
    }
    
    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public void pegaDados() throws SQLException {
        System.out.println("TEATEATAETAE");
        System.out.println(circuito);
        loadTempos("select * from tempos where `Circuito` = " + circuito);
    }

    public Map<String, String> getCircuitos() {
        return circuitos;
    }

    public void setCircuitos(Map<String, String> circuitos) {
        this.circuitos = circuitos;
    }

    public void onCircuitChange() {
        System.out.println("TESTETESTESTSE");
    }
}
