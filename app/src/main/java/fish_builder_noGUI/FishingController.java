import java.util.*;

public class FishingController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int playerEnergy = 100;
    private static int worldTimeMinutes = 480; // 08:00

    public static void main(String[] args) {
        System.out.println("== Aksi Fishing dimulai ==");
        performFishing();
    }

    public static void performFishing() {
        if (playerEnergy < 5) { //contoh
            System.out.println("Energi tidak cukup untuk memancing.");
            return;
        }
        //debugging
        System.out.println("Waktu dunia dihentikan...");
        addWorldTime(15);
        playerEnergy -= 5;
        System.out.println("Energi dikurangi 5. Energi sekarang: " + playerEnergy);

        // (contoh: halibut, di main contohnya halibut)
        FishBuilder builder = new FishConcreteBuilder();
        FishDirector director = new FishDirector();
        director.constructHalibut(builder);
        Fish fish = builder.getResult();

        System.out.println("Kamu mencoba menangkap: " + fish.getClass().getSimpleName() + " - " + fish);

        Fish.FishType type = fish.calculateSellPrice() >= 25 ? Fish.FishType.LEGENDARY :
                             fish.calculateSellPrice() >= 10 ? Fish.FishType.REGULAR : Fish.FishType.COMMON;

        int max = switch (type) {
            case COMMON -> 10;
            case REGULAR -> 100;
            case LEGENDARY -> 500;
        };
        int maxAttempts = switch (type) {
            case LEGENDARY -> 7;
            default -> 10;
        };

        int target = random.nextInt(max) + 1;
        System.out.println("Tebak angka antara 1 dan " + max + " (" + maxAttempts + " percobaan):");

        boolean success = false;
        for (int i = 1; i <= maxAttempts; i++) {
            System.out.print("Tebakan #" + i + ": ");
            int guess = scanner.nextInt();
            if (guess == target) {
                System.out.println("Selamat! Kamu berhasil menangkap ikan " + fish.getClass().getSimpleName());
                success = true;
                break;
            } else if (guess < target) {
                System.out.println("Terlalu kecil!");
            } else {
                System.out.println("Terlalu besar!");
            }
        }

        if (!success) {
            System.out.println("Sayang sekali, kamu gagal menangkap ikan.");
        }

        //resume waktu
        System.out.println("Waktu dunia berjalan kembali.");
        displayWorldTime();
    }

    private static void addWorldTime(int minutesToAdd) {
        worldTimeMinutes += minutesToAdd;
    }

    private static void displayWorldTime() {
        int hours = (worldTimeMinutes / 60) % 24;
        int minutes = worldTimeMinutes % 60;
        System.out.printf("Waktu sekarang: %02d:%02d\n", hours, minutes);
    }
}
