## Issues Resolved
      
### Teamcity SSH not working

Whatever Teamcity does with SSH wasn't working with Fedora 33.  
It was rejecting the ssh-rsa key the Teamcity user was presenting.  It
worked fine with the command line ssh, but not whatever Teamcity was using.

By using this command:
```bash
sudo journalctl _COMM=sshd
```
There would be an error like this:

```
userauth_pubkey: key type ssh-rsa not in PubkeyAcceptedKeyTypes [preauth]
```
                            
The solutionis to edit /etc/crypto-policies/back-ends/opensshserv
er.config
and add *rsa-ssh* to the end of the *PubkeyAcceptedKeyTypes* line.

### No Passwd for sudo
In order for the script to run dnf, we need to run as super user but
we can't use a prompt for the password.  We need to edit the sudoers
file using visudo.  First, uncomment the line with *Cmnd_Alias SOFTWARE* and
add */usr/bin/dnf* to the end of it.  Then near the bottom of the file, 
add this line:
```
%wheel  ALL=    NOPASSWD: SOFTWARE
```




