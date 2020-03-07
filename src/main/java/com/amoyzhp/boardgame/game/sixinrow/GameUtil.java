package com.amoyzhp.boardgame.game.sixinrow;

import com.amoyzhp.boardgame.game.sixinrow.constant.GameConst;
import com.amoyzhp.boardgame.game.sixinrow.core.Action;
import com.amoyzhp.boardgame.game.sixinrow.core.Agent;
import com.amoyzhp.boardgame.game.sixinrow.core.Environment;
import com.amoyzhp.boardgame.game.sixinrow.enums.Player;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class GameUtil {

    private static String FILE_PATH = "./data/";
    private static final Logger LOG = LoggerFactory.getLogger(GameUtil.class);


    public static Action readAction(Element actionEl){
        Action action = new Action();
        Iterator<Element> actionIt =  actionEl.elementIterator();
        if(!actionIt.hasNext()){
            LOG.debug("action xml has next is false");
            return action;
        }
        Element move = actionIt.next();

        action.setX1(Integer.parseInt(move.element("x").getText()));
        action.setY1(Integer.valueOf(move.elementText("y")));
        if(!actionIt.hasNext()){
            LOG.debug("action xml has next is false");
            return action;
        }
        move = actionIt.next();
        action.setX2(Integer.parseInt(move.element("x").getText()));
        action.setY2(Integer.valueOf(move.elementText("y")));
        if(!actionIt.hasNext()){
            LOG.debug("action xml has next is false");
            return action;
        }
        action.setPlayer(Player.paraseValue(Integer.parseInt(actionIt.next().getText())));
        return action;
    }

    public static GameWrapper readGameHistoryXml(String filename){
        GameWrapper wrapper = new GameWrapper();
        wrapper.init();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(FILE_PATH+ filename);
            Element root = document.getRootElement();
            LinkedList<Action> historyActions = new LinkedList<>();
            for (Iterator<Element> it = root.elementIterator("step"); it.hasNext();) {
                Element step = it.next();
                Iterator<Element> stepIt = step.elementIterator();
                //timestep
                Element timeStepEl = stepIt.next();
                // action
                Action action = readAction(stepIt.next());
                historyActions.add(action);
            }
            wrapper.getEnv().getGameState().setHistoryActions(historyActions);

        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            return wrapper;
        }
    }
    public static void writeGameHistoryToXml(GameWrapper wrapper, String filename){
        Environment env = wrapper.getEnv();
        Agent agent = wrapper.getAgent();
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("game");
        Element playerEl = root.addElement("player");
        if(agent.getPlayer() == Player.BLACK) {
            playerEl.addElement("Black").addText("AI");
            playerEl.addElement("White").addText("HUNMAN");
        } else {
            playerEl.addElement("Black").addText("HUNMAN");
            playerEl.addElement("White").addText("AI");
        }
        LinkedList<Action> historyActions = env.getGameState().getHistoryActions();
        Element actionEl;
        Element moveEl;
        Element stepEl;
        Action action;
        for(int i = 0; i < historyActions.size(); i++){
            stepEl = root.addElement("step");
            stepEl.addElement("timestep").addText(String.valueOf(i+1));
            action = historyActions.get(i);
            actionEl = stepEl.addElement("action");
            moveEl = actionEl.addElement("move");
            moveEl.addElement("x").addText(String.valueOf( action.getX1()));
            moveEl.addElement("y").addText(String.valueOf(action.getY1()));
            moveEl = actionEl.addElement("move");
            moveEl.addElement("x").addText(String.valueOf( action.getX2()));
            moveEl.addElement("y").addText(String.valueOf(action.getY2()));
            actionEl.addElement("player").addText(String.valueOf(action.getPlayer()));
        }
        // lets write to a file
        try (FileWriter fileWriter = new FileWriter(FILE_PATH + filename)) {
            XMLWriter writer = new XMLWriter(fileWriter);
            writer.write( document );
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
