# _Specialized web app_ _**`Gift-It`**_

**Allows you to perform an order of gifts and souvenirs with the original design.**

(EPAM Task - Web Application With Layered Architecture)

 The application is implemented using the Servlet and JSP technology. 
 Architecture corresponds Layered Architecture and MVC.
 The interface is internationalized and supports the choice of languages: Russian and English.
 
 The data is stored in the database. Access technology JDBC with pattern DAO.
 
 A complete java doc has been created for this application, available on [https://chmutsergei.github.io/Gift-It/](https://chmutsergei.github.io/Gift-It/)
 
 # Task description

This service allows you to order a gift for yourself or your loved ones. 
    
The site catalog includes a huge number of different types of items, such as T-shirts, cups, mouse pads and other souvenirs.     
In addition to selecting products from the catalog, you can use your own logos and images to create a unique design. 
The client makes an order from the site's assortment, choosing an existing design or creating his own appearance.
If you have questions, the client can ask it. After making the order, the client can leave comment on the quality of the items.
There is an opportunity to become a designer for a fee and place their designs / products. 
When approved, the products will be displayed in the site directory, and the designers themselves will receive a percentage of their sales.
In this system, users are distinguished: unregistered user (Guest), registered user (User), Moderator, Designer.
 Guest has the ability to view products and reviews from the official range.

The user also has more features:
- to order items from the catalog, creating its own style for it
- leave a comment about the quality of the product / delete it
- ask a question to the administrator
- view a list of previously purchased items
- editing contact information from personal account

Designer features:
- publication of items in the catalog
- receiving part of the profit from sales

Moderator:
- manages user feedback and comments.
- block users

Administrator Responsibilities:
- acceptance / processing of new orders
- Appointment of Experts, Designers and Moderators
- consideration of applications for adding goods to the range
- manage all users
- catalog management
- answers to users' questions

Use cases:
- 1 User Registration
- 2 Login
- 3 Item Search
- 4 Adding item to cart
- 5 Removing items from the cart
- 6 Checkout
- 7 Add a comment
- 8 Delete a comment
- 9 Ask a question
- 10 Login to moderators section
- 11 Review Management
- 12 user lock
- 13 Entrance to the section for designers
- 14 Sending Design
- 15 Add product to catalog
- 16 Login to personal account
- 17 View items in your account
- 18 Editing personal data
- 19 Appointment of a Designer, Moderator
- 20 Deleting a Designer
- 21 Assigning user rights
- 22 Deleting user
- 23 Removal (blocking) of items from the catalog
- 24 Answer to user question
- 25 View Orders

 
 
  # Usage
  
  The application is coded in Java, using Maven to manage the project.
  
  Download and unzip project /Gift-It-master/
  
  You must edit file **database.properties** from directory _Giftit/src/main/resources/_
            
   ->>change user key and password key to access Database
    
   Example:
   user=root
   password=1234
   
   Run script file **giftit.sql** from directory _Giftit/src/main/resources/_
  to roll the database.
  This script will **not add an image of the items**, so they **will not be displayed when the application is running**. 
  
  The remaining functions will not be affected.

  ### Prerequisites
  
 The project has been tested on the following Java versions:
 * JDK 8
 
 All other dependencies are handled through Maven, and noted in the included POM file.
 
 ### Test App With Profiles
 
  This application support 4 roles
 
 | Role     | Description                                       |
 |----------|-----------------------                            |
 | ROLE_USER| order items from the catalog, creating its own style for it, leave a review about the quality of the product / delete it, ask a question to the administrator|
 | ROLE_MODERATOR |manages user feedback and comments, block users |
 | ROLE_DESIGNER |publication of items in the catalog, receiving part of the profit from sales |
 | ROLE_ADMIN |acceptance / processing of new orders, Appointment of Experts, Designers and Moderato, consideration of applications for adding goods to the rang, manage all users, answers to users' questions|
 
For ADMIN role use USERNAME "admin" PASSWORD "1"

For DESIGNER role use USERNAME "des" PASSWORD "1"

For MODERATOR role use USERNAME "moder" PASSWORD "1"

And also Users : USERNAME "user1" PASSWORD "1", USERNAME "user2" PASSWORD "1", USERNAME "user3" PASSWORD "1"


 ### Installing
 
 The project can be installed by creating the war file and deploying it into a server.
 
 ### Running
 
 To run the project locally in an embedded server just use the following Maven command for deploying to Tomcat (after create database):
 
 ```
 mvn tomcat7:run
 ```
 
 With this the project will be accessible at [http://localhost:8080/giftit].
  
 
 # Screenshots
 
 ![sc1](https://user-images.githubusercontent.com/42671888/54277330-9c3f7880-45a0-11e9-9373-f28de770aca7.JPG)
 ![sc2](https://user-images.githubusercontent.com/42671888/54277361-afeadf00-45a0-11e9-92a8-741c073b54aa.JPG)
 ![sc3](https://user-images.githubusercontent.com/42671888/54277377-bb3e0a80-45a0-11e9-845d-d52dd5b56998.JPG)
 ![sc4](https://user-images.githubusercontent.com/42671888/54277407-d577e880-45a0-11e9-9652-c4e9a495ce61.JPG)
![sc5](https://user-images.githubusercontent.com/42671888/54277425-ddd02380-45a0-11e9-9fcc-e7e1feafda5f.JPG)
![sc6](https://user-images.githubusercontent.com/42671888/54277434-e6285e80-45a0-11e9-9d4e-9ce02e5e6a95.JPG)
![sc7](https://user-images.githubusercontent.com/42671888/54277440-ede80300-45a0-11e9-87fc-6e91bcac4304.JPG)
![sc8](https://user-images.githubusercontent.com/42671888/54277445-f50f1100-45a0-11e9-9380-ef7a5b1c2f8b.JPG)

 
