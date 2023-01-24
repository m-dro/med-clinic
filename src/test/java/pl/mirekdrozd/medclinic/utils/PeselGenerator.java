package pl.mirekdrozd.medclinic.utils;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class PeselGenerator {

    static String calculateChecksum(String pesel) {
        int peselLength = pesel.length();

        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int digit, sum = 0, checksum;
        for (int i = 0; i < peselLength; i++) {
            char c = pesel.charAt(i);
            digit = Integer.parseInt(String.valueOf(c));
            sum += digit * weights[i];
        }
        checksum = 10 - (sum % 10);
        if (checksum == 10) {
            checksum = 0;
        }
        return String.valueOf(checksum);
    }

    static String generatePeselWithoutChecksum() {
        int digitsToSkip = 2;
        String dateOfBirth = LocalDate.now().toString().substring(digitsToSkip).replaceAll("-", "");
        int min = 1;
        int max = 9;
        String seriesAndGender = "";
        for (int i = 0; i < 4; i++) {
            int randomIntInRange = ThreadLocalRandom.current().nextInt(min, max + 1);
            seriesAndGender += String.valueOf(randomIntInRange);
        }
        return dateOfBirth + seriesAndGender;
    }

    public static String generateValidPesel() {
        String peselWithoutChecksum = generatePeselWithoutChecksum();
        String checksum = calculateChecksum(peselWithoutChecksum);
        return peselWithoutChecksum + checksum;
    }

}
