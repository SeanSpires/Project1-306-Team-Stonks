# Project1-306

## Team Stonks

| GitHub | Name | UPI |
| ------- | ----------- | ----------- |     
| SeanSpires | Sean Spires | sspi841| 
| WaiHaiDere | Tyger Kong | tkon583| 
| pabloSushibar | Ryan Lim | rlim083| 
| MarioSinovcic | Mario Sinovcic | msin595 | 
| joshiefu | Joshua Fu | jfu047| 

## Building and running the JAR

<details>
  <summary><strong>Building the JAR</strong> </summary>
  
  1. Import `src` into IDE
  2. Build jar from IDE
      * [Eclipse](https://www.cs.utexas.edu/~scottm/cs307/handouts/Eclipse%20Help/jarInEclipse.htm)
      
      * Intellij 
      
        <details>
        
        1. `File` -> `Project Structure` -> `Artifacts`
        2. Create a new artifact -> JAR -> `From modules with dependencies`
        3. Select the main class -> `OK`
        4. `Apply` -> `OK`
        5. `Build` -> `Build Artifacts`
        6. Select your artifact and build
        7. The JAR file should now be in `out/artifacts/`

        </details>
</details>

<details>
  <summary><strong>Running the JAR</strong> </summary>
  
  1. Ensure your input files are in the same directory as the JAR
  2. Navigate to JAR directory in terminal
  3. Run the JAR using `java -jar [JARName].jar [InputFileName].dot [NumberOfProcessors] <Options>`
      * Options include:
          * | Parameter | Use |
            | ------- | ----------- |          
            | −p < N > | Use N cores for execution in parallel (default  is  sequential)' |
            | −v | Visualise the search |
            | −o [fileName] |   Output file is named [fileName] |

</details>
  
## Wiki
The wiki of this repository contains detailed information about our project. This includes:
* Background of the problem
* Our approach to the solution
* Code documentation
* Project planning 
* Meeting minutes
* Pair programming logs
