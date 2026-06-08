# ArsTracker

The purpose of this program is to allow for the tracking of characters from the Ars Magica roleplaying game, and later on to automate some of the calculations.

# Structure
The directory "commands" holds all classes interfacing between the objects and user input / terminal (Controller).
The directory "terminal" holds abstract structures dealing with user input and the user input itself (View).

Launcher is what controls the initialization of the program, and where the program returns to after a command is called, similar to a shell.

The directory "terminal" holds abstract structures and DataSource, which is where all database interactions are implemented.

The directory "models" holds the classes and enums that will be used to represent characters.

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

# Refactoring Goals, Process, and Progress
Currently, data processing, user input, and updating the DB is too intertwined making it difficult to expand on what I have.
The primary goal of refactoring are to actually split those (the MVC structure which I thought I was following). 

As such, the post-refactor structure/workflow should look like this:
    DB call to load a Character object and all of its internals
    Outputting parts of the Character object
    User input to edit Character object (via methods connected to the Character object or other important ones)
    DB call to update the Character

Small question though: will the updates be done by the Character class or the static classes?

Also add a bool array + enum to Character to track which sections were changed and need to be updated in the DB
    Or find some better way to make sure I'm not just calling update on all fields

# Possible (but unlikely) future improvements
A backstack for returning to the previous screen with the same UI loaded.