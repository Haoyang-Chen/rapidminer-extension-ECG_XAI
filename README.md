# rapidminer-extension-ECG_XAI

## How to Install

1. Install RapidMiner Studio https://rapidminer.com

2. Open RapidMiner Studio and select **Extensions -> Marketplace**, search and install '**Python Scripting**' extension. 

3. Clone this repository or unzip the project.

4. You will need to install a JDK if you don't have it. https://www.oracle.com/java/technologies/downloads/

5. Install Gradle 8.3 from https://gradle.org/next-steps/?version=8.3&format=bin

6. Move the folder **gradle-8.3** to where you want to store it.

7. Add an environment variable as

   ```sh
   export PATH="***YOUR LOCAL PATH***/gradle-8.3/bin:$PATH"
   ```

8. Go to this repository 's folder in your local storage, use the command line: 

   ```sh
   gradle installExtension
   ```

9. Open RapidMiner Studio, you will find the ECG_XAI folder available in your Operators Panel.

10. Select from the menu: **File -> Import Process** and select the project file **project.rmp** to open it in RM.
