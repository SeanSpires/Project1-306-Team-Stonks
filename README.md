# Project1-306




## Guidelines

> Make changes on new branches with appropriate names.

> Move the tasks to the correct places in the backlog.

> https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html




## From Project Brief
MilestonesThere are three milestones for the project implementation.

### Plan
Develop a plan for your project, using a Work Breakdown Structure (WBS), derive a NetworkDiagram from the WBS to show dependences between the tasks and put this into a Gantt Chart fordetailed planning.

**Basic milestone**

For the most basic milestone your implementation needs to be able to:

•Read the input file and number of processors and create an output file

•The output file needs to contain a valid schedule (which does not need to be optimal at thispoint).


**Final milestone**

The final milestone is the complete implementation. It needs to be able to (inorder of importance),

•Create an optimal schedule for small input graphs in reasonable time (say less than 30 minutes)

•Have a parallel version of the search that demonstrates speedup in comparison to the sequentialversion

•Have a meaningful and interesting live visualisation of the search5.


### Assessment
Total for this project:  **50%**

• Project plan  **(5%)**, due week 2(Tue  30  Jul  9:30am)

• Basic milestone, **(15%)**, demo/interview/implementation, week4 (implementation due Mon 12Aug 9:30am; Mon/Tue/Wed/Thu 12/13/14/15  Aug  demo/interviews)

• Final milestone, due week 6(implementation due Mon 26 Aug 9:30am; Mon/Tue/Wed/Thu 26/27/28/29Aug demo/interviews;reportdue  30  Aug  12:30pm; peerevaluation due 30 Aug 11:59pm)

– Demo/interview/implementation, **(20%)**

– Written report, **(10%)**

– Confidential peer evaluation form (each student) via TeamMates

The milestones will be graded according to various criteria.
These are functionality (finding optimal schedule), speed (time to find the optimal schedule ), high quality of coding standards, comments, documentation and testing. This project has the particular challenge of the contradicting objectives of high software engineering standards of design and code, and the need to have very fast execution speed.

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
