# ldap-server-sync

This project uses Quarkus, the Supersonic Subatomic Java Framework.

# Pre-Setup
1. Make sure to rename the .env.renamethis file in the root directory to .env and configure database credentials
2. Also, rename the file application-rename.properties to application.properties
3. You need a actual ldap server running with some users (ldif sample of a user) and configure it in application.properties file

#### User LDIF Example

dn: cn=John Skully,cn=lower,ou=employees,dc=example,dc=com

cn: John Skully

gidnumber: 502

givenname: John

mobile: 99738286723

objectclass: inetOrgPerson

objectclass: posixAccount

objectclass: top

sn: Skully

uid: john123

uidnumber: 1004

userpassword: {MD5}SjFdcHQoBWQZu7Sq/TrWIA==

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```
