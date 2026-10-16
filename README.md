# AI-Őrszem – Android alapprojekt

Magyar nyelvű Android rendszerállapot-kijelző. Az első verzió megjeleníti az akkumulátor töltöttségét, a szabad tárhelyet és a RAM-állapotot, valamint figyelmeztet a kevés tárhelyre vagy alacsony töltöttségre.

**Fontos:** ez egy alapverzió, nem teljes AI-alapú biztonsági termék. Nincs benne AI-elemzés, folyamatos háttérfigyelés vagy értesítés.

## APK készítése

A `main` ágra feltöltéskor a GitHub Actions megpróbálja elkészíteni a debug APK-t. **Actions → Android APK Build → sikeres futás → Artifacts → AIOrszem-debug-APK**.

Android Gradle Plugin 8.7.3, Gradle 8.9, Java 17, Android SDK 35. Nincs szükség API-kulcsra. Az alkalmazás nem küld adatot hálózatra.

A projekt összeállítása után a GitHub Actions futás eredményét ellenőrizni kell.
