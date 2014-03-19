
经过仔细的试验，这个帖子里说的比较准确

http://www.worldhello.net/2010/11/30/2166.html

git diff --cached  是 HEAD和index的差异
git  diff 是 working tree 和 index 的差异
git  diff HEAD 是 working tree 和 HEAD 的差异


如果把 working tree 代号为1
       index 代号为2
       repository 代号为3

则上面三个分别是 2-3,  1-2,  1-3 之前的差异。

http://365git.tumblr.com/post/3464927214/getting-a-diff-between-the-working-tree-and-other


但是需要注意的是，如果index里没有某个文件， 则即使在 working tree有这个文件， 也不会在diff结果里显示

如果  index 有某个文件， 则 git diff 是 working tree 和 index的差异，
如果 index 没有某个文件， 则 git diff  不显示和这个文件相关的部分。
git diff HEAD 的意义一样， 必须在index里有这个文件，才会参与diff

