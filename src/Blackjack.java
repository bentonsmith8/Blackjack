import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
    public static ArrayList<Player> playerList = new ArrayList<Player>();
    public static void main(String[] args) throws Exception {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Blackjack\n");
        //Create new shuffled deck
        Deck deck = new Deck();
        //Deal to each player
        Player dealer = new Player("dealer");
        playerList.add(dealer);
        Player player1 = new Player("player1");
        playerList.add(player1);
        // Player player2 = new Player("player2");
        // playerList.add(player2);
        
        do {
            //Give 2 cards to each player
            for (Player p : playerList) {
                p.takeCard(deck.drawCard());
                p.takeCard(deck.drawCard());
            }
            //Print to log
            logCards();
            System.out.println();
            playPlayers(scnr, deck);
            System.out.println();
            System.out.println("\nDealer Plays:");
            playDealer(scnr, deck);
            System.out.println("\nResults: ");
            logAllCards();
            System.out.println("\n" + winner() + "\n");
            for (Player p : playerList) {
                p.clearHand();
            }
            deck = checkDeck(deck);
            System.out.println("Play again? (q to quit)");
        } while (!scnr.nextLine().equals("q"));
    }
    
    private static Deck checkDeck(Deck d) {
        if (d.size() <= 8)
        {
            d.add(new Deck());
        }
        return d;
    }

    private static String winner() {
        String winnername = "nobody";
        int closest = 0;
        for (Player player : playerList) {
            if (player.getValue() > closest && player.getValue() <= 21) {
                closest = player.getValue();
                winnername = player.getName();
            }
        }
        if (closest == 21)
            return "Blackjack! Congratulations " + winnername + "!";
        else 
            return "Congratulations " + winnername + "!";
    }

    private static void logAllCards() {
        for (Player player : playerList) {
            System.out.println(player.getName() + "'s hand: " + handToString(player.getHand()));
        }

    }

    private static void playDealer(Scanner scnr, Deck deck) {
        Player dealer = playerList.get(0);
        System.out.println("Dealer's hand: " + handToString(dealer.getHand()));
        if (dealer.getValue() >= 17 && dealer.getValue() <= 21) {
            dealer.stand();
            System.out.println("Dealer stands.");
        }
        while (!dealer.isFolded() && !dealer.isStanding()) {
            dealer.takeCard(deck.drawCard());
            System.out.println("Dealer's hand: " + handToString(dealer.getHand()));
            if (dealer.getValue() >= 17 && dealer.getValue() <= 21) {
                dealer.stand();
                System.out.println("Dealer stands.");
            }
            if (dealer.getValue() > 21)
                System.out.println("Dealer busts.");
        }
    }

    private static void playPlayers(Scanner scnr, Deck deck)
    {
        for (Player player : playerList) {
            while(!player.getName().equals("dealer") && (!player.isFolded() && !player.isStanding()))
            {
                logCards(player);
                if (player.getValue() != 21)
                {
                    System.out.println("Hit or Stand " + player.getName() + "?");
                    String resp = scnr.nextLine();
                    if (resp.equals("h"))
                    {
                        player.takeCard(deck.drawCard());
                        logCards(player);
                        if (player.isFolded())
                            System.out.println("Bust!");
                        if (player.getValue() == 21)
                            player.stand();
                    }
                    else if(resp.equals("s"))
                    {
                        player.stand();
                    }
    
                }
            }
        }
    }
    
    private static void logCards(Player player) {
        System.out.println(player.getName() + "'s hand: " + handToString(player.getHand()));
    }

    private static void logCards() {
        for (Player player : playerList) {
            if (player.getName().equalsIgnoreCase("dealer"))
                System.out.println("Dealer's hand: " + hiddenHandToString(player.getHand()));
            else
                System.out.println(player.getName() + "'s hand: " + handToString(player.getHand()));
        }
    }
    public static String handToString(ArrayList<Card> arrlist)
    {
        String tmpstr = "";
        for (Card card : arrlist) {
            tmpstr = tmpstr.concat(card.toString() + " ");
        }
        return tmpstr;
    }
    public static String hiddenHandToString(ArrayList<Card> arrlist){
        String tmpstr = "";
        tmpstr = tmpstr.concat(arrlist.get(0).toString() + " ");
        for (int i = 1; i < arrlist.size(); i++) {
            tmpstr = tmpstr.concat("XX ");
        }
        return tmpstr;
    }
}