/*
 * Author: Michael Thai
 * A simple library to parse Shadowverse card data using Shadowverse-portal API found at https://gist.github.com/theabhishek2511/dfd54989013254324cc4d67f1dbc9f7f
 * 
 */

package shadowverseparser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Hoshi
 */
public class ShadowverseParser {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        // 3 parameters format=json , lang=en or ja(japanese) , clan (0-8 for the different classes) 
        String json = readUrl("https://shadowverse-portal.com/api/v1/cards?format=json&lang=en&clan=2");
        JSONObject jsonobject = new JSONObject(json);
        List<Cards> cardList = new ArrayList<Cards>();
        cardList = parse(jsonobject);

        //Test
        for (int i = 0; i < cardList.size(); i++){
            System.out.println("Name: " + cardList.get(i).getCard_name());
            System.out.print("Attack: " + cardList.get(i).getAtk());
            System.out.print("  HP: " + cardList.get(i).getLife());
            System.out.print("  Playpoint Cost: " + cardList.get(i).getCost());
            System.out.println("    Effect: " + cardList.get(i).getSkill_disc());
            System.out.println("Evo Effect: " + cardList.get(i).getEvo_skill_disc());
            System.out.println("Card Lore: "  +cardList.get(i).getDescription());
            System.out.println();
        }
    }
    
    
    //Downloads JSON data from a URL as a string
    private static String readUrl(String urlString) throws Exception {

        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    
    //Parses the JSON Object converted from the URL 
    public static List<Cards> parse(JSONObject cards){
        
        JSONArray list = null;
        JSONObject data = null;
        
        //Try to find the "cards" array in the json
        try{
            data = cards.getJSONObject("data");
            list = data.getJSONArray("cards");
        } catch (JSONException | NullPointerException e){
            e.printStackTrace();
        }
        
        return getCards(list);
    }
    
    //Create a list of cards by looping through the JSONArray and retrieving data
    private static List<Cards> getCards(JSONArray cards){
        
        int noCards = cards.length();
        List<Cards> list = new ArrayList<Cards>();
        Cards card = null;
        
        for (int i = 0; i < noCards; i++){
            try{
                card = getCard(cards.getJSONObject(i));
                list.add(card);
            } catch (JSONException | NullPointerException e){
                e.printStackTrace();
            }
        }

        return list;
    }
    
    //Creates each individual card object and returns it to getCards()
    private static Cards getCard(JSONObject cardElement){
        Cards card = null;
        
        int card_id = 0;
        int card_set_id = 0;
        String card_name = "--";
        int char_type = 0;
        String tribe_name = "--";
        String skill_disc = "--";
        String evo_skill_disc = "--";
        int cost = 0;
        int atk = 0;
        int life = 0;
        int evo_atk = 0;
        int evo_life = 0;
        int rarity = 0;
        String description = "--";
        String evo_description = "--";
        
        try {
            if (cardElement.has("card_id")){
                card_id = cardElement.getInt("card_id");
            }
            if (cardElement.has("card_set_id")){
                card_set_id = cardElement.getInt("card_set_id");
            }
            if (cardElement.has("card_name")){
                card_name = cardElement.getString("card_name");
            }
            if (cardElement.has("char_type")){
                char_type = cardElement.getInt("char_type");
            }
            if (cardElement.has("tribe_name")){
                tribe_name = cardElement.getString("tribe_name");
            }
            if (cardElement.has("skill_disc")){
                skill_disc = cardElement.getString("skill_disc");
            }
            if (cardElement.has("evo_skill_disc")){
                evo_skill_disc = cardElement.getString("evo_skill_disc");
            }
            if (cardElement.has("cost")){
                cost = cardElement.getInt("cost");
            }
            if (cardElement.has("atk")){
                atk = cardElement.getInt("atk");
            }
            if (cardElement.has("life")){
                life = cardElement.getInt("life");
            }
            if (cardElement.has("evo_atk")){
                evo_atk = cardElement.getInt("evo_atk");
            }
            if (cardElement.has("evo_life")){
                evo_life = cardElement.getInt("evo_life");
            }
            if (cardElement.has("rarity")){
                rarity = cardElement.getInt("rarity");
            }
            if (cardElement.has("description")){
                description = cardElement.getString("description");
            }
            if (cardElement.has("evo_description")){
                evo_description = cardElement.getString("evo_description");
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        
        card = new Cards(card_id, card_set_id, card_name, char_type, tribe_name, skill_disc, evo_skill_disc,  cost, atk, life, evo_atk, evo_life, rarity, description, evo_description);
        
        return card;
    }
}
