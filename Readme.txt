103062215    謝昇祐    assignment5


程式說明:

我使用了Lab做的socket的架構，不同的點在於傳送的部分比較複雜。

當server只收到一個input的時候，傳一個"W"回去，意思是告訴那個client要印出Waiting字樣。

而收到兩個的時候則進行比對，如果一樣就傳"T"，代表input相同，進行加分和移動以及更新Word。

如果不同則傳"F"，代表input不同，印出wrong answer字樣即可，不必更新Word。

由於更新的Word要一致，所以random的值由server產生，一起傳回去給兩個client。

而遊戲架構的部分是使用上次作業的，沒有太多更改。


遇到的問題:

我一開始將random的值放在client產生，結果造成兩個client的Word不同，

後來將random的產生放到server，再一併傳給client就解決這個問題了。