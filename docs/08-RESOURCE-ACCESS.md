#Resource access
Kirin supports the loading of resource files (currently text only).  It uses GWT's [`ClientBundle`](http://www.gwtproject.org/doc/latest/DevGuideClientBundle.html) interface.

## Accessing resources
Resource files should live in a Java package folder, **or** in a resources folder.  Access is through an interface which extends `com.google.gwt.resources.client.ClientBundle`.

Create an instance of this interface using `GWT.create` as shown:

    package com.futureplatforms.resourcetest.core.resources;
    
    import com.google.gwt.core.client.GWT;
    import com.google.gwt.resources.client.ClientBundle;
    import com.google.gwt.resources.client.TextResource;
    
    public interface Resources extends ClientBundle {
    	public static final Resources INSTANCE = GWT.create(Resources.class);
    	
    	@Source("initdb.sql")
    	TextResource initdb();
    }
    
To load `initdb.sql` elsewhere in your app:

`String initdb_sql = Resources.INSTANCE.initdb().getText();`

In this case, the resource file should be located in:
`ResourceTest/core/src/main/java/com/futureplatforms/resourcetest/core/resources/initdb.sql`

**OR**
If you prefer to separate your resources, use:
`ResourceTest/core/src/main/resources/com/futureplatforms/resourcetest/core/resources/initdb.sql`


##Ensure your resources are bundled correctly

Double-check your core project's `.pom` file to ensure any resource files will be bundled in to your jar correctly.

    <build>
      ...
      <resources>
        <resource>
          <directory>src/main/java</directory>
          <includes>
            <!-- Include everything in this folder.
                 If you change this, ensure
                 to include at least **/*.java,
                 **/*.gwt.xml, and any patterns for any
                 resources you include -->
            <include>**/*</include>
          </includes>
        </resource>
        <resource>
          <directory>src/main/resources</directory>
          <includes>
            <!-- Include everything in this folder --> 
            <include>**/*</include>
          </includes>
        </resource>
        ...
      </resources>
      ...
    </build>

###This uses `GWT.create`, how does it work on Android?

Android's version of the `GWT` classes is located in the `kirin-gwt-stub` project.  The `GWT.create` method in there now looks for a class extending `ClientBundle` and synthesizes an instance which provides access to the files.