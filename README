+ Introduction

This is a Calendar and Scheduling app that is used to create, edit and store schedules. Each schedule is based on a
template and each schedule can have multiple events added to it. Users can interact with the app through
UserUI, ScheduleUI and TemplateUI.

We have three types of schedule templates: Monthly Template, Weekly Template and Daily Template.
We also have three corresponding types of schedules: Monthly Schedule, Weekly Schedule and Daily Schedule
We have five different types of users including: Regular User, Public User, Private User, Trial User, Admin User

+ Instructions

We have three initial templates with different types (Monthly, Weekly and Daily) and we can create monthly schedules,
weekly schedules and daily schedules according to the templates.

First we can choose to sign up as a new user (admin or regular or public or private), log in, or use this app as a trial
user in the welcome menu.

We can sign up using an email address and a password. A password can have three types, too weak, weak, and good. Users can
only choose a password that is at least weak. User id will be provided to the user from the console after
successfully sign up.

We can log in using our registered email and password combination. Or we can choose 'Forgot Password', which would create
an "email" that is a text file generated outside of our program and named as our user email. This text file contains our
general user information and a temporary password that could be used to login back for one single login.
After using that temporary password to login, we would be asked to change a permanent password that is at least "weak",
which would be our permanent password. If we request the "email" accidentally, we can still use our original password
to login as well.

After we successfully log in (or if we choose to be a trial user),
The main menu will show a list of things we can do according to our user type.

-Regular/Public/Private
If we are a regular user or public user or private user, we can interact with template such as see all the templates
and select one.
After we select a template by inputting the corresponding option before each template, we can make our own schedule
based on it. Creating a schedule requires a schedule name, a status (public/private/friend-only) and a schedule date in a
specific format (the format is yyyy MM for a monthly schedule and yyyy MM dd for a daily schedule and a weekly schedule).
After we create a schedule, we can add/delete event by inputting the name, start time and end time of this event.
Again, the start time and end time for a event has specific format (the format is dd hh:mm for a monthly schedule,
hh:mm for a daily schedule and MM dd hh:mm for a weekly schedule).
The event will only be added to the schedule if it follows the restrictions set by the chosen template.
For example, if the template has a min duration of event set to 1 hour, then each event will only be added if
it's at least 1 hour long.

Regular users can interact with the schedules in the following ways:
1. can login and select a schedule template in order to create their own schedule
2. see a list of their previously created schedules
3. see a list of schedules their friends shared with them
4. see a list of public schedules
5. modify their own schedules (e.g. add new event, delete existing event, change schedule status
from to public, private or friend-only)
6. view all the events in their schedule
7. undo deleted events or deleted schedules as long as the user doesn't exit the program
8. undo changed status as long as the user doesn't exit the schedule they are currently editing
9. edit schedules shared with them through messages
10. edit schedules shared with them through friend-only setting. To edit someone else's schedule, you must enter
the schedule id of this schedule. A list of friend-shared schedules with their id's will be displayed before prompting
you to enter a schedule id. If you would like to remember the schedule id's shared with you through messages, you can
also input them to edit those schedules.
Note: A public or private user can do exactly the same things as regular user, except that their creations are always
private or public.

Next time when regular/public/private user need to login, they can simply see their previously created schedules or to
create a new
schedule. They can also see the schedules their friends share with them or any existing public schedules. They can
also edit any schedules their friends shared with them or any public schedules.

We can also choose to change our password, change email, edit our friend list, or send messages.
If we choose to edit our friend list, we can add/delete any other regular/public/private user as a friend by choosing
from the email list that given.

If we choose to send messages, we can interact with message through sending, deleting, reading and replying messages.
We need to enter the message content
and the receiver id to send the message. The message can attach a creation (schedule) so that receiver can interact
with the schedule. After sending, our personal chat history and receiver's personal chat history will store this message
which contains sender id, receiver id, content, related creation and time.
By using the function that linking a schedule in a message, users can check all messages in their chat history,
and then directly edit a particular schedule linked in the message chosen.
If we want to send message to an admin user, the commonInbox for admin will receive their messages.

-Trial
If we are a trial user, we can do the same set of things as a regular user, except the trial user's user
information and schedule creations will not be stored once they exit the program. Trial user cannot send messages
to other users or admin, edit friend list, change password or email. They can create friend-only schedules just to test
the function but their schedules won't be shared with anyone.

-Admin

Admin user can interact with the schedules in the following ways:
1. can create their own schedules like a regular user but they can only make their schedules public or private
(friend-only is not allowed).
2. can also edit any user's creations (e.g. change status to public or private, delete their schedules, add/delete events)

If we are an admin user, we can see all templates, create new default templates, and edit templates.
After we create a default template (set the type -Daily or Monthly- and use the default values for the time restrictions),
we can edit the template attributes such as min time between events, min/max time of an event. Once the restrictions are
modified, users who want to create a new schedule according to this template must follow the new restrictions.

We can choose to change our password, change email, change welcome message, suspend a user, freeze users, unfreeze users,
create another admin user, or send messages.

By choosing changing welcome message, we can create a welcome message that users can see when they log in and change the
welcome message.
By choosing suspending a user, we can enter the user's email we want to suspend and the number of days(x) we want to suspend
that user. Then, that user will not be able to log in after x number of days.
By choosing freezing users, we need to enter the number of days(x). Then, at the time we choose to freeze users, accounts of
anyone who has not logged in since x number of days ago will be frozen and no one can interact with their creations until
they log back in to unfreeze their account. Or if any of the admin users choose to unfreeze users, then all users that are
frozen before will become unfreeze.
By choosing creating another admin user, we can create accounts for other admin users.
By choosing to send messages, admin users can delete, reply, send and read messages in the commonInbox.
Admin users can send a message to one permanent
user(regular/public/private), a list of permanent user or all permanent users except admin users at a time.
By entering message content and
receiver id(s) , admin can send message to other users and it will store in the commonInbox and their own personal
chat history.

We can exit this app through the welcome menu or main menu.

+ Files outside of the program
Note: The following files do not exist at the first time running our program.
But they will be created as long as we run the program.

We have template data that store every templates.(TemplateData.ser)
We have user info to store user data.(UserInfo.ser)
We have scheduleTemp file to store data that maps a schedule id to a template id.(ScheduleData2.ser)
We have ChatHistory file to store users' chat history that maps a user id to his messages.(ChatHistory.ser)
We have CommonInbox file to store messages sent or received by admin users. (CommonInbox.ser)
We also have schedule list to store data that maps a user id to a list of schedule id's.(ScheduleData.ser)
We have all emails of users who have been suspended now.(SuspendAccount.ser)
We have all emails of users who have been frozen now.(FreezeAccount.ser)
We have all permanent user's last login time.(UserLoginTime.ser)
We have the temporary password for any users if they chose 'Forgot password' before and did not login using their temporary
password.(TempInfo.ser)
We have a text file contains the welcome message set by any admin user.(WelcomeMsg.txt)

Also, if a permanent user chose "Forgot password", a text file('email') named by his/her user's email will be generated.
This file will contain this user's information and the temporary password that could be used to login.

