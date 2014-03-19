[user]
        name = liangwang
        email = liangwang@travelzen.com
#[diff]
#    external = extDiff
[gui]
    spellingdictionary = none
[mergetool]
    trustExitCode = false
[merge]
    tool = p4merge
[mergetool "p4merge"]
    cmd = p4merge \"$BASE\" \"$LOCAL\" \"$REMOTE\" \"$MERGED\"
[giggle]
        main-window-maximized = false
        main-window-geometry = 700x550+78+77
[svn]
        rmdir = true
