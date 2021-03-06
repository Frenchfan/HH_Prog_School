import java.util.Scanner;

public class Main2 {
    static long totalmoney = 0;
    static int nAccouts, nManagers;
    static long maximumbonus;
    static long delta = 0;

    static int[] accounts;
    static int limit = 200;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //по условию 1≤N≤100 000, 1≤M≤100 000 -
        // то есть, для кол-ва счетов и менеджеров достаточно int
        //Запрашиваем первую строку (кол-во счетов и менеджеров через пробел)
        String firstLine = scanner.nextLine();
        //Разделяем кол-во счетов и менеджеров
        String[] nAccoutsnManagers = firstLine.split(" ");
        nAccouts = Integer.parseInt(nAccoutsnManagers[0]);
        nManagers = Integer.parseInt(nAccoutsnManagers[1]);
        accounts = new int[nAccouts];
        //Запрашиваем суммы на N счетах
        for (int i = 0; i < nAccouts; i++) {
            accounts[i] = scanner.nextInt();
        }
        //Выводим результат
        System.out.println(bonus());
    }

    private static long bonus() {
        //по условию задачи может быть до 100 000 счетов, на каждом из которых
        // до 100 000 000. То есть, для расчета полной суммы денег нужен long
        for (int i = 0; i < accounts.length; i++) {
            totalmoney += accounts[i];
        }
        //Проверяем условие - Если денег на счетах компании не хватит на то, чтобы выдать
        // премию хотя бы по 1 у.е. - значит премии не будет, и нужно вывести 0.
        if (totalmoney / nManagers < 1) return 0;
        //возможен вариант, когда на 2-х менеджеров нужно поделить деньги с тысяч счетов по 100 млн у.е. каждый,
        //поэтому вновь воспользуемся long
        maximumbonus = totalmoney / nManagers;
        if (checkbonus(maximumbonus)) return maximumbonus;
        decreasetolimit(limit);
        for (long i = maximumbonus; i >= maximumbonus-delta; i--) {
                if (checkbonus(i)) return i;
        }
    return 0;
    }



    private static void decreasetolimit(int limit) {
        if (delta == 0) {
            delta = maximumbonus / 2;
        }
        while (delta>limit) {
            if (checkbonus(maximumbonus-delta) && checkbonus(maximumbonus-delta/2)) {
                delta=delta/2;
                decreasetolimit(limit);
            } else if (checkbonus(maximumbonus-delta) && !checkbonus(maximumbonus-delta/2)) {
                    maximumbonus=maximumbonus-delta/2;
                    delta/=2;
                    decreasetolimit(limit);
            } else if (!checkbonus(maximumbonus-delta) && checkbonus(maximumbonus-delta/2)) {
                if (checkbonus(maximumbonus-delta/2+1)) {
                    delta/=2;
                    decreasetolimit(limit);
                } else {
                    maximumbonus=maximumbonus-delta/2;
                    decreasetolimit(limit);
                }
            } else {
                maximumbonus=maximumbonus-delta;
                delta=maximumbonus/2;
                decreasetolimit(limit);
            }

        }
    }

    private static boolean checkbonus(long maximumbonus) {
        long nAccountstoPayBonus=0;
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] >= maximumbonus) {
                nAccountstoPayBonus = nAccountstoPayBonus + accounts[i] / maximumbonus;
                if (nAccountstoPayBonus >= nManagers) return true;
            }
        }
        return false;
    }
}

