# Java-POS-System
## Created using JDK 10

## Technologies Used:
  - H2 was used for the database. It’s an embedded database. 
  - Hibernate framework was used for connection to the database. 
  - Lambda Expressions have been used throughout the project.
  - Listeners have been used throughout the project too from changeListeners to actionListeners. 
  - ControlsFX was also used to add Auto completion to text fields. (We tried using combo boxes for it before that, but that got really hard to navigate as the products in the database increased) 
  - Criteria API was also used with Hibernate for selecting from the database. (Took a lot of help from the internet for this part and it took the most amount of time)
  - Streams API was used to process collections multiple times and mostly to map one object’s collection to another object’s collection. 

## H2 and the Road to Embedded Database:
Before I was trying to use socket programming for databases. And the database was supposed to be on a server. But I got stuck trying to implement that for a while and it turned out to be too complex. So I gave up on that and started to work on local databases. In the process I learned about H2 and we adopted it. Cause if the database did not exist on the machine, it would just create it and that saved me the trouble of having to constantly synchronize databases on my machines. Now the database was on a file and that increased the overall portability of the project.

## API:
Hibernate is used to create tables in the database and it’s used to completely communicate with the database. The entity classes create their respective tables. Criteria API is used with hibernate to select rows from tables based on different conditions. Streams API is used to convert the list of entities we get from the database through Criteria API to wrapper objects that we used to populate tableViews throughout the application. Criteria API was also used to compute sums of transactions and returns for reports too. ControlFX’s auto-completion was added to multiple search bars and one combobox. 

## Specifications:
There are three types of employees. Manager, Sales Rep and Customer Rep. All of the roles can access the inventory, employee records, financial reports and are able to log out. But there are some differences in their access. Sales Rep and Customer Rep cannot update the information of any employee except themselves and they cannot change their own roles. They also can’t add or remove employees. The manager can update the information of all employees except their passwords. No role can see the passwords of other employees. 

Other similarities include the fact that all roles can add, update and delete products. When products are deleted their visibility is set to false so they don’t appear in the inventory but they are still kept in the database because we need them for records of old returns and sales. This also applies to Employees. If the manager deletes an employee only the visibility of that employee is updated, the records of the employee are still stored in the database and track is kept of which employee sold which items. 

Manager and Sales Rep can access the Sales view and the previous sales view. In the sales view they can sell products to a customer. It generates the bill and updates the quantity of the respective products. In the previous sales view, we can check previous transactions and the items they contained. You enter the transaction id in the search bar and click search, or select an autocompletion. That populates the table below with products and also displays the total tax, subtotal and total price along with the date of the transaction. 

Manager and Customer Rep can access the Return and Previous Returns view. You can only return items from a sale only once. Even if you return one item out of 10 in a sale, you cannot return any of the remaining 9 at a later date. You can return multiple products at once. But  once they have been returned, you can’t return again. Here too, there’s a search bar and a search button. There’s autocompletion on the search bar and it only gives transaction ids of transactions/sales that don’t have any returned items. The previous returns view shows the previous returns and all the products contained in each return. You have to provide the return id in the search bar for it and it also supports autocompletion. 

Finally in the financial report view, we provide a given date and it gives us the statistics of the day. It provides us with the start of day cash, end of day cash, generated income, the tax on generated income and amount of money refunded. For the report, we used CriteriaQuery and CriteriaBuilder to retrieve all transactions before the given day or on the given day and then used streams to calculate the sum of all transactions.
If the program runs and the employee table is empty, has no visible employees or the database file does not exist then the program creates the required tables if they don’t exist and inserts a default employee ‘Sahil Patel’ the manager and his password is set to 111 by default. All views in which something is being added or updated, we have added checks so the required fields can never be left empty. We also have made sure that every text field only takes valid input. We achieved that by adding ChangeListeners to every text field and whenever the input was invalid we just removed it from the text.

## Improvements for the future:
There are some bugs in the application. For example when a manager updates the information of an employee he sets the password of the employee to ‘*****’. We tried many different ways to implement the feature of hiding the passwords including using CellFactory on the tableColumn but every way had its own bugs. We finally landed on this and hope to fix this by adding some extra variables. 

Also some other improvements might include new reports. Sales Rep performance reports and statistics on how many sales, each sales rep has made in a given time. Also reports for Customer Rep for how many returns they have made in a given time. These reports will only be available for the Manager.

Currently the embedded database works, but because of it the distribution of the application would mean that every application would have its own database. So every employee would have to share the application on one computer to make it work. The solution to it would be to use a centralized database. Instead of trying to create our own server that has its own database. We can just use the free postgresql database provided by Heroku. To implement it, all we would have to change is the database url, the sql Dialect, the username and password in the hibernate configuration file. It should theoretically work, but with how many problems we have had regarding the database we can’t say for sure until we try it. 
