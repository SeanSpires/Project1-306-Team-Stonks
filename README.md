# Project1-306

## Team Stonks
Team Members:
1) WaiHaiDere: Tyger Kong tkon583
2) MarioSinovcic: Mario Sinovcic msin595
3) pabloSushibar: Ryan Lim Rlim083
4) SeanSpires: Sean Spires sspi841
5) joshiefu: Joshua Fu jfu047

## Building and running the jar

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
The wiki of this project contains documentation of the classes we used as well as implementation of our pl

# Useful Git Commands

#### Initialising Repositories

| Command | Purpose |
| ------- | ----------- |
| `git init` | Initialise a local repository |
| `git clone ssh://git@github.com/[username]/[repository-name].git` | Clone a remote repo as a local repository |

#### Basic Commands

| Command | Purpose |
| ------- | ----------- |
| `git status` | See the files that have been changed |
| `git add [file-name.txt]` | Add a file to the staging area |
| `git add .` | Add all new and changed files to the staging area |
| `git commit -m "[commit message]"` | Commit staged changes with a short description |
| `git rm -r [file-name.txt]` | Remove a file (or folder) |

#### Branching & Merging

| Command | Purpose |
| ------- | ----------- |
| `git branch` | List branches (the asterisk denotes the current branch) |
| `git branch -a` | List all branches (local and remote) |
| `git branch [branch name]` | Create a new branch |
| `git branch -d [branch name]` | Delete a branch |
| `git push origin --delete [branch name]` | Delete a remote branch |
| `git checkout -b [branch name]` | Create a new branch and switch to it |
| `git checkout -b [branch name] origin/[branch name]` | Clone a remote branch and switch to it |
| `git branch -m [old branch name] [new branch name]` | Rename a local branch |
| `git checkout [branch name]` | Switch to a branch |
| `git checkout -` | Switch to the branch last checked out |
| `git checkout -- [file-name.txt]` | Discard changes to a file |
| `git merge [branch name]` | Merge a branch into the active branch |
| `git merge [source branch] [target branch]` | Merge a branch into a target branch |
| `git stash` | Stash changes in a dirty working directory |
| `git stash clear` | Remove all stashed entries |

#### Sharing & Updating Projects

| Command | Purpose |
| ------- | ----------- |
| `git push origin [branch name]` | Push branch to remote repository |
| `git push -u origin [branch name]` | Push changes to remote repository (and remember the branch) |
| `git push` | Push changes to remote repository (remembered branch) |
| `git push origin --delete [branch name]` | Delete a remote branch |
| `git pull` | Fetch and merge remote repository changes to working directory |
| `git pull origin [branch name]` | Pull changes from remote repository |
| `git remote add origin ssh://git@github.com/[username]/[repository-name].git` | Connect local repository to a specified remote repository |
| `git remote set-url origin ssh://git@github.com/[username]/[repository-name].git` | Set a repository's origin branch to SSH |

#### Inspection & Comparison

| Command | Purpose |
| ------- | ----------- |
| `git log` | View changes |
| `git log --summary` | View changes (detailed) |
| `git diff [source branch] [target branch]` | Preview changes before merging |
| `git grep "foo()"` | Search the working directory for foo() |

#### Undo & Reset
| Command | Purpose |
| ------- | ----------- |
| `git reset --hard origin/master` | Discard any local changes and reset working directory with remote repository |
