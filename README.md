# ArsTracker

The purpose of this program is to allow for the tracking of characters from the Ars Magica roleplaying game, and later on to automate some of the calculations.

# Structure
All actions that a user can take are implemented in the directory "commands", each of which implement Command, which is an interface that requires only the method execute() so that the Command Design Pattern can be utilized. For each file in that directory, if it ends with "Commmand", then it is not dependent on user input, and vice versa. So for example, CharacterEditor utilizes user input and LoadCampaignCommand does not.

Launcher is what controls the initialization of the program, and where the program returns to after a command is called, similar to a shell.

The directory "terminal" holds abstract structures and DataSource, which is where all database interactions are implemented.

The directory "characters" holds the classes and enums that will be used to represent characters.

The directories "controllers" and "displays" are from an older version of the project, which was going to be implemented in JavaFX. I decided that I would rather make a terminal-esque program, and so those are left for possible future implementation. For now, they are unused.

# Roadmap
Or, when this project will be considered complete enough.

The user should be able to get a list of characters, and select one to either see the information of or change it. They should also be able to see the description of any virtue or flaw the character holds.

1. There should exist a list of campaigns
2. The user can get a list of characters from a selected campaign and select one (LoadCampaignCommand)
3. The user can see the information about that character (CharacterOutputCommand)
4. The user can update the abilities and characteristics of the character (CharacterEditor)
5. The user should be able to see the virtues and flaws of a given character
6. The user should be able to check the description of virtues and flaws
7. The user should be able to create new characters (CharacterCreator)

## Theoretical Changes
1. Combine commands and non-commands (execute as the only human interaction)