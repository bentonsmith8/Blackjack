import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
    public static ArrayList<Player> playerList = new ArrayList<Player>();
    public static void main(String[] args) throws Exception {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Blackjack\n");
        final int STARTING_MONEY = 1000;
        //Create new shuffled deck
        Deck deck = new Deck();
        //Prompt for players
        System.out.print("Enter number of Players: ");
        int numPlayers = scnr.nextInt();
        scnr.nextLine();
        System.out.println();
        playerList.add(new Player("dealer", -1));

        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name of player " + (i + 1));
            String playername = scnr.nextLine();
            playerList.add(new Player(playername, STARTING_MONEY));
        }
        int wager;
        //Game Loop
        do {
            //Set Wagers for each player
            wager = 100;
            for (int i = 1; i < playerList.size(); i++) {
                placeWager(scnr, wager, i);
            }
            //Give 2 cards to each player
            for (int i = 0; i < playerList.size(); i++) {
                i += drawInitCards(scnr, deck, i);
            }
            //Print to log
            logCards();
            System.out.println();

            //Ask the players to play
            playPlayers(scnr, deck);

            //Play the dealer
            System.out.println("\nDealer Plays:");
            playDealer(scnr, deck);

            //Calculate and print results
            System.out.println("\nResults: ");
            logAllCards();

            //Find which players won
            for (int j = 1; j < playerList.size(); j++) {
                playerList.get(j).setResult(compareToDealer(playerList.get(j)));
            }
            System.out.println();

            //Print the result of each player
            for (int i = 1; i < playerList.size(); i++) {
                printPlayerResult(i);
            }

            //Clear the player's hand for the next game
            for (Player p : playerList) {
                p.clearHand();
            }

            //Check to see if the deck needs to be added to
            deck = checkDeck(deck);

            //Check the remaining balance of each player
            balanceCheck();
            if (playerList.size() == 1)
            {
                System.out.println("All players are broke!\nExiting game...");
                break;
            }
            //Prompt to play again
            System.out.println("Play again? (q to quit)");
        } while (!scnr.nextLine().equals("q"));
        scnr.close();
    }

    private static void placeWager(Scanner scnr, int wager, int i) {
        String resp;
        Player player = playerList.get(i);
        System.out.println(player.getName() + "'s balance: $" + player.getMoney());
        System.out.println("Set wager (Default is $100):");
        resp = scnr.nextLine();
        if (resp.equals("") && player.getMoney() >= 100) {
            wager = 100;
        }
        else
        {
            boolean betPlaced = false;
            while (!betPlaced)
            {
                try {
                    wager = Integer.parseInt(resp);
                    if (wager > player.getMoney())
                    {
                        System.out.println("Balance too low! Choose a lower wager!");
                        resp = scnr.nextLine();
                    }
                    else if(wager <= 0)
                    {
                        System.out.println("Cannot wager $0!");
                        resp = scnr.nextLine();
                    }
                    else{
                        betPlaced = true;
                    }
                } catch (Exception e) {
                    System.out.println("Please enter an integer.");
                    resp = scnr.nextLine();
                }
            }
        }
        player.setWager(wager);
        player.addMoney(-wager);
    }

    private static int drawInitCards(Scanner scnr, Deck deck, int i) {
        int returnval = 0;
        Player p = playerList.get(i);
        p.takeCard(deck.drawCard());
        p.takeCard(deck.drawCard());
        if (i != 0)
        {
            returnval = checkForSplit(p, scnr, i, deck);
        }
        return returnval;
    }

    private static void printPlayerResult(int i) {
        Player p = playerList.get(i);
        Player ap;
        if (p.getName().substring(((p.getName().length() < 5) ? 0 : p.getName().length() - 5), p.getName().length()).equals("split"))
            ap = playerList.get(i - 1);
        else
            ap = p;
        if (p.getResult() == 2) {
            System.out.println("Blackjack! " + p.getName() + " won!");
            ap.addMoney((int)(ap.getWager() * (3 / 2.0)));
            System.out.println(ap.getName() + " wins $" + (int)(ap.getWager() * (5 / 2.0)));    
        }
        else if (p.getResult() == 1)
        {
            System.out.println(p.getName() + " won!");
            ap.addMoney(ap.getWager() * 2);
            System.out.println(ap.getName() + " wins $" + (ap.getWager() * 2));    
        }
        else if (p.getResult() == 0)
        {
            System.out.println(p.getName() + " tied with dealer, wager has been restored.");
            ap.addMoney(ap.getWager());
        }
        else if (p.getResult() == -1)
        {
            System.out.println("Better luck next time " + p.getName());
        }
        if (ap != p)
        {
            playerList.remove(i);
        }
        System.out.println();
    }
    
    private static int checkForSplit(Player p, Scanner scnr, int index, Deck deck) {
        int returnval = 0;
        if (p.getHand().get(0).getRank() == p.getHand().get(1).getRank())
        {
            logCards(p);
            System.out.println(p.getName() + ", would you like to split?");
            if (scnr.nextLine().equals("y")) {
                playerList.add(index + 1, new Player(p.getName() + "-split", p.getWager()));
                Player ps = playerList.get(index + 1);
                p.addMoney(-p.getWager());
                p.setWager(p.getWager() * 2);
                ps.addMoney(-p.getWager());
                ps.takeCard(p.getHand().get(1));
                p.playCard(1);
                returnval = 1;
            } 
        }
        return returnval;
    }

    private static void balanceCheck() {
        for (int i = 1; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            if (player.getMoney() == 0)
            {
                System.out.println(player.getName() + " is broke!");
                playerList.remove(i);
                --i;
            }
        }
    }

    private static Deck checkDeck(Deck d) {
        if (d.size() <= playerList.size() * 2 + 4)
        {
            d.add(new Deck());
        }
        return d;
    }

    private static int compareToDealer(Player p) {
        //1 is beat dealer
        //0 is tie
        //-1 is loss
        int result = 0;
        Player dealer = playerList.get(0);
        if (p.getValue() > 21)
            result = -1;
        else if (p.getValue() == 21 && dealer.getValue() != 21){
            result = 2;
        } else if (p.getValue() == 21 && dealer.getValue() == 21) {
            result = 0;
        } else if (dealer.getValue() > 21) {
            result = 1;
        } else if (p.getValue() == dealer.getValue()) {
            result = 0;
        } else if(p.getValue() > dealer.getValue()) {
            result = 1;
        } else if(p.getValue() < dealer.getValue()) {
            result = -1;
        }
        return result;
    }

    private static void logAllCards() {
        for (Player player : playerList) {
            System.out.println(player.getName() + "'s hand: " + handToString(player.getHand()) + "(" + player.getValue() + ")" );
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
                if (player.getValue() == 21)
                    player.stand();
                else
                {
                    System.out.println("Hit (h) or Stand (s) " + player.getName() + "?");
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
        System.out.println();
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