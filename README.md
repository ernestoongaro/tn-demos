#TN Demos from Presentation

Import TN-demos-repo.zip to your jaspersoft repository - it will create a set of folders under Public - > Samples -> TIBCO NOW. Please do this on a sandbox system!

##Bursting Sample

First in JasperReports Server chage  <property name="enableSaveToHostFS" value="true"/> in applicationContext.xml to enable save to file system. Change the variable "reportsPath" to make certain the folder exists in your path. Also check authentication!
Now you can run the sample with groovy burst.groovy! Check the REST API docs for how to edit the schedule. To do this in a production system, I recommend the JRS Java client: https://github.com/Jaspersoft/jrs-rest-java-client

##Excel Features
Simple export the report to Excel to see it action, inspect the JRXML from Studio to see the properties. The properties references are here: http://jasperreports.sourceforge.net/config.reference.html (Just search for xls in the page)

##Visualize.js Demo
You'll need to host the /tn/ folder under a web server (or tomcat under /webapps/tn). Simply browse to index.html and should work for you!


