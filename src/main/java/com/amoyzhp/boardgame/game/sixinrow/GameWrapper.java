package com.amoyzhp.boardgame.game.sixinrow;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class GameWrapper {
    private SixInRowEnv env;
    private Agent agent;

    public void init(){
        this.env = new SixInRowEnv();
        this.agent = new Agent();
        this.env.init();
        this.agent.init();
    }

    public void setEnv(SixInRowEnv env){
        this.env = env;
    }

    public void setAgent(Agent agent){
        this.agent = agent;
    }

    public SixInRowEnv getEnv(){
        return this.env;
    }

    public Agent getAgent(){
        return this.agent;
    }

    public void writeGameToXml(SixInRowEnv env, Agent agent){
        Document dom;
        Element e = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            Element rootEle = dom.createElement("gameState");

            Element players = dom.createElement("players");
            e = dom.createElement("BLACK");
            if(agent.getPlayer() == GameConst.BLACK){
                e.setTextContent("AI");
            } else {
                e.setTextContent("HUMAN");
            }
            players.appendChild(e);

            e = dom.createElement("WHITE");
            if(agent.getPlayer() == GameConst.WHITE){
                e.setTextContent("AI");
            } else {
                e.setTextContent("HUMAN");
            }
            players.appendChild(e);
            rootEle.appendChild(players);

            LinkedList<Action> historyActions = env.getGameState().getHistoryActions();
            Element actionEl = null;
            Element moveEl = null;
            Element xEl = null;
            Element yEl = null;
            Element playerEl = null;
            Element tiemstepEl = null;
            int cnt = 1;
            for(Action action : historyActions){
                actionEl = dom.createElement("Action");

                // move 1
                moveEl = dom.createElement("Move");
                xEl = dom.createElement("X");
                yEl = dom.createElement("Y");
                xEl.setTextContent(String.valueOf(action.getX1()));
                yEl.setTextContent(String.valueOf(action.getY1()));
                moveEl.appendChild(xEl);
                moveEl.appendChild(yEl);
                actionEl.appendChild(moveEl);
                // move 2
                moveEl = dom.createElement("Move");
                xEl = dom.createElement("X");
                yEl = dom.createElement("Y");
                xEl.setTextContent(String.valueOf(action.getX2()));
                yEl.setTextContent(String.valueOf(action.getY2()));
                moveEl.appendChild(xEl);
                moveEl.appendChild(yEl);
                actionEl.appendChild(moveEl);

                playerEl = dom.createElement("Player");
                playerEl.setTextContent(String.valueOf(action.getPlayer()));
                actionEl.appendChild(playerEl);

                tiemstepEl = dom.createElement("Timestep");
                tiemstepEl.setTextContent(String.valueOf(cnt));
                actionEl.appendChild(tiemstepEl);
                cnt++;

                rootEle.appendChild(actionEl);
            }

            dom.appendChild(rootEle);
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream("./test.xml")));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }


        }catch (ParserConfigurationException pce){

        }
    }

}
