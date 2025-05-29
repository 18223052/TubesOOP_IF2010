package controller;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
import main.GamePanel;
import object.FishItem;
import object.FishItem.FishCategory;
import object.FishLocation;



public class FishingController {
    GamePanel gp;
    private Random random = new Random();
    private FishItem currentFish;
    private int targetNumber;
    private int attemptsLeft;
    private int maxAttempts;
    private int maxNumber;

    private String fishingMessage = "";

    public FishingController(GamePanel gp){
        this.gp = gp;
    }

    public void startFishing(FishLocation location){
        this.fishingMessage = "";
        
        List<FishItem> catchableFish = getCatchableFish(location);

        if (catchableFish.isEmpty()){
            System.out.println("hmm tidak ada ikan yang tertarik dengan umpan mu");
            gp.ui.setDialog("hmm tidak ada ikan yang tertarik dengan umpan mu");
            gp.setGameState(GamePanel.dialogState);
            gp.ui.clearDialog();
            endFishing(false);
            return;
        }

        currentFish = catchableFish.get(random.nextInt(catchableFish.size()));
        System.out.println("A " + currentFish.getName() + " is on the hook");
        gp.ui.setDialog("A " + currentFish.getName() + " is on the hook");
        gp.setGameState(GamePanel.dialogState);
        gp.repaint();
        setupGuessingGame(currentFish.getFishCategory());

        promptForGuess();
    }

    private List<FishItem> getCatchableFish(FishLocation curreLocation){
        List<FishItem> allPossibleFish = gp.itemFactory.getAllFishItems();
        List<FishItem> catchableNow = new ArrayList<>();

        
        Season currentSeason = gp.gameTime.getCurrentSeason();
        int currentDayInSeason = gp.gameTime.getDayInCurrentSeason();
        WeatherType currentWeather = gp.weatherManager.getWeatherForDay(currentDayInSeason);
        GameTime currentGlobGameTime = gp.gameTime.getCurrentTime();

        for (FishItem fish : allPossibleFish){
            if (fish.isCatchable(currentSeason, currentWeather, currentGlobGameTime, curreLocation)){
                catchableNow.add(fish);
            }
        }
        return catchableNow;
    }

    private void setupGuessingGame(FishCategory category) {
        switch (category) {
            case COMMON:
                maxNumber = 10;
                maxAttempts = 10;
                break;
            case REGULAR:
                maxNumber = 100;
                maxAttempts = 10;
                break;
            case LEGENDARY:
                maxNumber = 500;
                maxAttempts = 7;
                break;
            default:
                maxNumber = 10;
                maxAttempts = 5;
                break;
        }
        targetNumber = random.nextInt(maxNumber) + 1; 
        attemptsLeft = maxAttempts;
    }

    private void promptForGuess(){
        System.out.println("DEBUG: Entering promptForGuess(). Kesempatan tersisa: " + attemptsLeft);
        SwingUtilities.invokeLater(() -> {
            System.out.println("DEBUG: Running JOptionPane on EDT.");
            String input = JOptionPane.showInputDialog(
                gp, 
                "Tebak angka dari 1 hingga " + maxNumber + ".\nKesempatan tersisa: " + attemptsLeft,
                "Fishing Minigame - Guess the Fish!",
                JOptionPane.PLAIN_MESSAGE
            );
            System.out.println("DEBUG: JOptionPane returned. Input: " + (input != null ? input : "null"));

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int guess = Integer.parseInt(input.trim());
                    processGuess(guess);
                } catch (NumberFormatException e) {
                    gp.ui.setDialog("Invalid input. Masukkan angka.");
                    gp.setGameState(GamePanel.dialogState);
                    gp.repaint();
                    if (attemptsLeft > 0) {
                        promptForGuess();
                    } else {
                        endFishing(false);
                    }
                }
            } else {
                gp.ui.setDialog("Memancing dibatalkan. Ikannya maburr!");
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
                endFishing(false);
            }
        });
        System.out.println("DEBUG: Exiting promptForGuess().");
    }

    public void processGuess(int guess) {

        attemptsLeft--;
        this.fishingMessage = ""; 

        if (guess == targetNumber) {
            gp.ui.setDialog("Kamu menebak dengan benar! Kamu berhasi menangkap " + currentFish.getName() + "!");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            gp.player.getInventory().addItem(currentFish);
            endFishing(true);
        } else if (attemptsLeft <= 0) {
            gp.ui.setDialog("Gabisa main lagi! kesempatan kamu sudah habis " + currentFish.getName() + " maburrrr.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            endFishing(false);
        } else {
            if (guess < targetNumber) {
                gp.ui.setDialog("Terlalu rendah cooo! Kesempatan tersisa: " + attemptsLeft);
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
            } else {
                gp.ui.setDialog("Nah kalo ini terlalu tinggi! Kesempatan tersisa: " + attemptsLeft);
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
            }

            promptForGuess();
        }
    }

    private void endFishing(boolean success) {
        gp.gameTime.addTime(15);  
        // gp.ui.setDialog("Fishing action ended.");
        // gp.setGameState(GamePanel.dialogState);
        this.fishingMessage = ""; 
    }


    public String getFishingMessage() {
        return fishingMessage;
    }
    
}