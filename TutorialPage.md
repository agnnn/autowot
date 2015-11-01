# Tutorial #

## Hello World Web Server ##

In this tutorial, we will create step-by-step your first web server with AutoWoT.
The goal of this first tutorial is to create a resource, which outputs `Hello <yourName>` and contains a web interface with a text field, allowing the user to enter his name and sending it via a `POST` request to the resource.

First we launch AutoWoT and create your first resource by dragging the item `RootResource` from the right list into the central pane. You will then be prompted for the name and the url of the resource. Simply enter:
  * Name: myMain
  * URI: /mymain
  * Type: Auto

Now your first resource is created! Now we want our resource to react to `POST`request and to generate a text field for entering the name of the user. We drag the element `Poster` onto the resource and get prompted for a name, a function to execute, type of the function, type of the argument and representation type, where "representation type" controls which kind of form is generated. As we want to pass a `String` via a text field, simply enter:
  * Name: postName
  * Function: postNameHandler
  * Type: Java
  * Argument Type: String
  * Representation Type: Text

Now we create a Getter, which reacts to `GET` requests. Drag and drop the item `Getter` onto the resource and enter to following data:
  * Name: getName
  * Function: getNameHandler
  * Type: Java

Now we can generate the server by clicking on "Generate Java Webserver". Enter any name or package structure that you like, make sure that "Create Standalone Application" is enabled and click on "Generate Code" to create the server.

Now you can find a new folder called `AutomaticPrototypes` in the same folder in which the AutoWoT application is located. Open Eclipse or any other IDE and import the content of the `AutomaticPrototypes` folder.

Locate the file Handler.java, open it and you will see the two functions 'getNameHandler' and 'postNameHandler'.

Next add a static field to the class holding the name: `protected static String name = "World";`

Then edit the body of the `getNameHandler` function to: `return "Hello " + name;`

Add one statement to the body of the function `postNameHandler`: `name = (String) posterVar;`

That was it! You can run the server and browse to `http://localhost:8080/mymain` and you should see the output "Hello World" and a text box below, enter your name, click on the submit button and enjoy!