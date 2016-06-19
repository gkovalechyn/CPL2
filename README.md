#CPL2: Electric Boogaloo

####Summary
These are all the projects required for the CPL thing to work.  
One thing that I didn't bother myself with is making it so that I didn't have to manually edit and recompile the server
every time the hashes changed. And by the way, for some retarded reason, the hashes were different in Java and in C++, so
I just used the CPL dll to get the hashes instead of re-calculating them every time.

####If you want this to work:
1. Use the plugin encryptor to encrypt the plugin.
2. Copy the encrypted plugin to the PluginLoader's plugin folder in the server.
3. Put the CPL dlls in the root folder of the server.
4. Start up the server, the CPL dll should generate a "CPL.log" file, from there you can get the SHA-256 hashes.
5. Get the key and IV that were generated from the plugin encryption and the SHA-256 hashes and set those values on the server.
6. Start up the plugin server and the minecraft server.
7. Watch the decryption fail.