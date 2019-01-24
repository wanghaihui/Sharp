# python2
import json, urllib2

config_url = "http://api.mengliaoba.cn/apiv5/getconfig.php?flag=newbase&ver=6.9.0"

result = urllib2.urlopen(config_url, None, 20)
string = result.read()

s = json.loads(string)

print "package com.pocketmusic.kshare.API;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\npublic class UrlKey {";
print "\tpublic static final String Default_Tag = \"%s\";" % s['tag']


for key in s['content']:
    print "\t/* %s */" % (key);
    for key_key in s['content'][key]:
        kk = (key + '_' +  key_key)
        print "\tpublic static final String %s = \"%s\";" % (kk.upper(), kk)

print "\n\tpublic static final Map<String, String> defaultUrls = new HashMap<String, String>() {\n\t\t/**\n\t\t *\n\t\t */\n\t\tprivate static final long serialVersionUID = 1L;\n\t\t{";
for key in s['content']:
    print "\t\t\t/* %s */" % (key);
    for key_key in s['content'][key]:
        kk = (key + '_' +  key_key)
        print "\t\t\tput(%s, \"%s\");" % (kk.upper(), s['content'][key][key_key]);

print "\n\t\t}\n\t};\n}\n";
