# Description
The RaspPi Java API is a basic collection of classes that handle HTTP requests being sent to a web server hosted on the Raspberry Pi. The API also contains a basic set of classes that stores the data sent back by the server into easy-to-understand objects.

## Variables
Using the RaspPi Java API you can create, update, view, and remove variables stored in an SQL database on the Raspberry Pi.

##### VariableObject()
A variable stored on the variable database hosted on the Pi has 4 attributes:
- **id** (Integer) - A unique ID used to identify the variable.
- **name** (String) - The name of the variable, exists for the ease of use for the developer.
- **type** (String) - The variable's type. Currently, the only types supported are:
    + Integer
    + Float
    + String
    + Boolean
- **value** (Object) - The variable's value.

##### VariableRegister()
The VariableRegister() handles the creation, retrieval, and removal of variables so that it can regulate the variable IDs, making sure each variable has a unique ID. Only one VariableRegister() exists, so multiple "instances" of the VariableRegister() are in fact the same instance with a different name. The VariableRegister() only handles the IDs of variables, not any of the attributes. The VariableRegister() stores local variable IDs in an ID Stack, which is simply a list of used IDs. The ID Stack contains only local variable IDs, so if there is a variable stored in the variable database that isn't used in the Java code, it isn't stored in the ID Stack.
- **IdStack** (List<Integer>) - A list of local variable IDs.

##### Creating a Variable
To create a variable meant to be stored on the variable database simply, you can do the following:
``` java
// Create a VariableObject without a name or value.
        VariableObject var1 = new VariableObject();
        /*
            var1:   id - null
                    name - null
                    type - null
                    value - null
         */
```
Initializing a variable without attributes will create a variable with **null** attributes. This is useful when attempting to load a variable that is already on the variable database. Note that the ***id*** and ***type*** cannot be set by the user. In order to get the VariableRegister() to assign an ID to the variable, the user must set a name to variable. For example, in the case above, our **var1** is initialized without a name, so it isn't assigned an ID. To give it an ID, we can simply give it a name by using **var1.setName("var1")**. Realizing that it doesn't have an ID as well, **var1** will ask the VariableRegister() to assign it one. Without an ID, the variable won't be stored in the variable database.
``` java
        // Create a VariableObject with a name.
        VariableObject var2 = new VariableObject("var2");
        /*
            var1:   id - [unique ID]
                    name - "var2"
                    type - null
                    value - null
         */
```
In the example above, **var2** is initialized with only a ***name***. Furthermore, because it is given a name, the VariableRegister() also assigns it a unique ID.
``` java
        // Create a VariableObject with a name and value.
        VariableObject var3 = new VariableObject("var2", 12);
        /*
            var1:   id - [unique ID]
                    name - "var3"
                    type - "integer"
                    value - 12
         */
```
In the example above, **var3** is initialized with both a ***name*** and ***value***. Since it is given a name, it is assigned a unique ID. Also, because it was given a value, the type is set. Note, that whenever the value is changed, the type is automatically set to the value's type.

##### Updating a Variable on the Variable Database
Local variables and variable on the database are only loosely synced, so that the minimal amount of HTTP requests is sent to the server. When a variable is assigned an ID, it is immediately put on the variable database. However, changes made to the **name** or **value** attributes are not immediately reflected on the variable database. To update the variable database with these changes, simply do the following:
``` java
VariableObject var1 = new VariableObject("var1", 12);
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12                 
            local -     id: 1, name: "var1", type: "integer", value: 12
         */
        
        var1.setName("var300");
        var1.setValue("abcdefg");
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12                 
            local -     id: 1, name: "var300", type: "string", value: "abcdefg"
         */
        
        var1.update();
        /*
            database -  id: 1, name: "var300", type: "string", value: "abcdefg"   
            local -     id: 1, name: "var300", type: "string", value: "abcdefg"
         */
```
##### Refreshing a Variable
After making changes to a variable, the user has the option to restore the variable with it's attributes stored on the variable database. This allows the user to sort of undo any changes made to the local variable. To do so, simply do the following:
``` java
        VariableObject var1 = new VariableObject("var1", 12);
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12
            local -     id: 1, name: "var1", type: "integer", value: 12
         */

        var1.setName("var300");
        var1.setValue("abcdefg");
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12
            local -     id: 1, name: "var300", type: "string", value: "abcdefg"
         */
         
        var1.refresh();
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12
            local -     id: 1, name: "var1", type: "integer", value: 12
         */

```
##### View Variable's Current Attributes on Variable Database
Before updating or refreshing a variable, the user may want to see it's currently stored attributes on the variable database. This can help the user decide if they should update or refresh. To do so, just do the following:
``` java
    VariableObject var1 = new VariableObject("var1", 12);
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12
            local -     id: 1, name: "var1", type: "integer", value: 12
         */

        var1.setName("var300");
        var1.setValue("abcdefg");
        /*
            database -  id: 1, name: "var1", type: "integer", value: 12
            local -     id: 1, name: "var300", type: "string", value: "abcdefg"
         */
        
        Map<String, Object> current = var1.viewRemote();
        /*
            current = {"id": 1, "name": "var1", "type": "integer", "value": 12}
         */

```
##### Load Variable into VariableObject()
In some cases, there is a variable the user needs that already exists on the variable database. The user can load the variable into an empty VariableObject(). To do so, do the follwowing:
```java
        /* 
            We want to load the following variable from the variable database:
                id: 36, name: "var40", type: "integer", value: 17
            
            First, we want to initialize an empty VariableObject().
         */
        VariableObject var40 = new VariableObject();
        // var40 - id: null, name: null, type: null, value: null
        /*
            Then we can call var.load(ID) where ID is the id of the variable we want to load. In this case, we want to use the ID 36.
         */
        
        var40.load(36);
        // var40 - id: 36, name: "var40", type: "integer", value: 17
```

In the example above, the user initializes an empty VariableObject(). Using an empty VariableObject() ensures that the variable isn't copied to a new ID, so that when the variable is updated or refreshed, it is referencing the correct variable.

##### Removing a Variable
In order to remove a variable from the variable database, it must be stored into a VariableObject(). Removing a variable is done by simply calling the **.remove()** method on a VariableObject(). This will remove remove the variable from the variable database, free it's variable ID, remove it from the ID Stack in the VariableRegister(), as well as set all of it's attributes to **null**. Essentially, it turns the VariableObject() into an empty one.









1
