# ArsTracker

The purpose of this program is to allow for the tracking of characters from the Ars Magica roleplaying game, and later on to automate some of the calculations.

# Structure
## File Structure
All actions that a user can take are implemented in the directory "commands", each of which implement Command, which is an interface that requires only the method execute() so that the Command Design Pattern can be utilized.

Launcher is what controls the initialization of the program, and where the program returns to after a command is called, similar to a shell.

The directory "terminal" holds abstract structures and DataSource, which is where all database interactions are implemented.

The directory "characters" holds the classes and enums that will be used to represent characters.

The directories "controllers" and "displays" are from an older version of the project, which was going to be implemented in JavaFX. I decided that I would rather make a terminal-esque program, and so those are left for possible future implementation. For now, they are unused.