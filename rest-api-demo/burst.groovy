/* 
JRS Bursting Example  
  Set  <property name="enableSaveToHostFS" value="true"/> in applicationContext.xml to enable save to file system
*/

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7')
import groovyx.net.http.RESTClient
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovy.io.FileType
import groovy.time.*

def reportsPath = "/Users/eongaro/Desktop/rest-api-demo"
def reportURI = "/public/Samples/TIBCO_NOW/Bursting_Sample/Monthly_Store_Snapshot"

def folder = new File(reportsPath)
assert folder.exists(), "The report folder ${reportsPath} does not exist!" 

//get input controls
def inputControls = new RESTClient('http://localhost:8080/jasperserver-pro/rest_v2/')
inputControls.auth.basic 'jasperadmin', 'jasperadmin'
def resp = inputControls.get( path : 'reports/public/Samples/TIBCO_NOW/Bursting_Sample/Monthly_Store_Snapshot/inputControls/values/', contentType: XML, headers : [Accept : 'application/xml'] ) 
    assert resp.status == 200

//set up schedule api call
def jrs = new HTTPBuilder('http://localhost:8080/jasperserver-pro/rest_v2/jobs/')
jrs.auth.basic 'jasperadmin', 'jasperadmin'

//for each input control
resp.data.inputControlState.options.option.value.eachWithIndex { icValue,index -> 
       if( icValue != "~NOTHING~" &&  icValue != "~NULL~" ) {
           println index + " " +icValue.toString()
           jrs.request(PUT, XML) {
         body = """
            <job>
                <baseOutputFilename>"""  +  icValue.toString() + """</baseOutputFilename>
                <repositoryDestination>
                    <folderURI>/public/</folderURI>
                    <saveToRepository>false</saveToRepository>
                    <outputLocalFolder>""" + reportsPath + """</outputLocalFolder>
                    <overwriteFiles>true</overwriteFiles>
                </repositoryDestination>
                <description/>
                <label>ManagerSnapshot-""" + icValue.toString() + """</label>
                <outputFormats>
                    <outputFormat>PDF</outputFormat>
                </outputFormats>
                <outputLocale/>
             <source> 
              <reportUnitURI>""" + reportURI + """</reportUnitURI>
               <parameters>
               <parameterValues>
                 <entry>
                   <key>store_manager</key><value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">""" + icValue.toString() + """</value>
                 </entry>
                 <entry>
                   <key>month</key><value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">May</value>
                 </entry>
                 </parameterValues>
               </parameters>
              </source>
           
               <simpleTrigger>
                    <startType>1</startType>
                    <timezone>Europe/Dublin</timezone>
                    <version>0</version>
                    <occurrenceCount>1</occurrenceCount>
                </simpleTrigger>
            </job>
            """
        headers.'Content-Type' = 'application/xml'
        }
     }
} 
