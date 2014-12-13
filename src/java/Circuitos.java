import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
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
    private String novoCircuito;
    private String novoTempo;
    private String novoAno;

    @PostConstruct
    public void init() {
        getCircuitosFromBD();
    }

    public void getCircuitosFromBD() {

        circuitos = new HashMap<String, String>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conexao = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/t2webdev", "root", "");
            stmt = (PreparedStatement) conexao.prepareStatement("SELECT `Circuito` FROM `tempos`");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("Circuito");
                if (!circuitos.containsValue(nome)) {
                    System.out.println("Add: " + nome);
                    circuitos.put(nome, nome);
                }
            }
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(Circuitos.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                String tempo = rs.getString("tempo");
                String ano = rs.getString("ano");
                System.out.println("Tempo: " + tempo);
                System.out.println("Ano: " + ano);
                listaTempos.add(new chartData(ano, tempo));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Circuitos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            createChart();
            rs.close();
            stmt.close();
            conexao.close();
        }
    }

    public void addTempo() throws SQLException {
        System.out.println("MASTER BLASTER CACAGASTER");
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        listaTempos = new ArrayList<>();
        System.out.println(novoCircuito+"',"+novoTempo+"',"+novoAno);
        try {
            conexao = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/t2webdev", "root", "");
            String s ="INSERT INTO `tempos`(`Circuito`, `tempo`, `ano`) VALUES ('"+novoCircuito+"','"+novoTempo+"','"+novoAno+"')";
            System.out.println(s);
            stmt = (PreparedStatement) conexao.prepareStatement(s);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Circuitos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            createChart();
            rs.close();
            stmt.close();
            conexao.close();
        }
    }

    public void createChart() {
        System.out.println(listaTempos.size());
        System.out.println("Dados para o circuito "+circuito);
        for(chartData d : listaTempos){
            System.out.println("Tempo da volta: "+d.getTempo());
            System.out.println("Tempo da ano: "+d.getAno());
        }
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public String getNovoCircuito() {
        return novoCircuito;
    }

    public void setNovoCircuito(String novoCircuito) {
        this.novoCircuito = novoCircuito;
    }

    public String getNovoTempo() {
        return novoTempo;
    }

    public void setNovoTempo(String novoTempo) {
        this.novoTempo = novoTempo;
    }

    public String getNovoAno() {
        return novoAno;
    }

    public void setNovoAno(String novoAno) {
        this.novoAno = novoAno;
    }

    public void pegaDados() throws SQLException {
        System.out.println("cric:"+circuito);
        loadTempos("SELECT * FROM `tempos` WHERE `Circuito` = '"+circuito+"'" );
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
