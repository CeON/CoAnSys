`tabela` <- read.table("/home/pdendek/dane_icm/2012-04-20-CEDRAM_N3_NEWPREDICATES/2012-04-22_20-51-35.csv", header=F, quote="\"")

names(tabela) = c("a@b.c","a@",
                  "CO_CONTR","MSC",
                  "KEY_PHRA","KEY_WORDS",
                  "CO_REF","CO_ISSN",
                  "CO_ISBN","FULL_INITZ","SAME","DBASE")
tmp = tabela

head(tmp)
tmp$DBASE <- NULL
# tmp$MSC <- NULL
tmp$CO_ISBN <- NULL
tmp$FULL_INITZ <- NULL
head(tmp)

tmp <- tmp[tmp$SAME!=0,  ]

tmp_diff <- tmp[tmp$SAME==-1,  ]
tmp_same <- tmp[tmp$SAME==1,  ]




nrow(tmp_same)/nrow(tmp_diff)

tmpNames = c("a@b.c","a@",
             "CO-CONTR", "MSC",
             "KEY-PHRA","KEY-WORDS",
             "CO-REF","CO-ISSN",
             "SAME")
# tmpNames
#plots describing OCCURENCE-TRUE-FLASE relation
for(i in 1:(ncol(tmp)-1)){
#  i=1
  print("===================================")
  ending="."  
#   if(i==2) ending="st"
#   else if(i==3) ending="nd"
#   else if(i==4) ending="rd"
#   else if(i>4) ending="th"  
  
  
  tetext = paste("Wskazówka", tmpNames[i], sep=" ")
  print(tetext)
  tab = table(tmp[,i],tmp[,ncol(tmp)])
  colnames(tab) = c("różni autorzy","ten sam autor")
  print(tab)
  
  t2 = t(tab)
  
  rna = rownames(tab)
  #rna[rna=="-1"]="null"
  #rownames(tab) = rna
#   for(z in 1:nrow(tab)){
#     numm = t2[1,z]+t2[2,z]
#     print(numm)
#   }  
  #tab[tab[,1]==0,1]=0.1
  #tab[tab[,2]==0,2]=0.1

  for(zzz in 1:nrow(tab)){
    for(xxx in 1:ncol(tab)){
      if(tab[zzz,xxx]==0){
        tab[zzz,xxx]=0.1
      } 
    }
  }
  
#   filename = paste(getwd(),"/R_AND/DAS/",tmpNames[i-1],"_","Clue.pdf", sep="")
  #filename = paste(getwd(),"/THESIS/chap005/Clues/",tmpNames[i-1],"_","Clue.pdf", sep="")
#   pdf(file=filename, height=4, width=5)
#   par(xpd=T, mar=par()$mar+c(0,0,0,4))
  
  ?barplot
barplot(t(tab),
            main=tetext,
#             ylab="Occurence Number",
#             xlab="Feature Value",
            space=0,1,
            legend=T,
            #beside=T,
            cex.axis=0.8, 
            las=1,
            cex=0.8,
            col=c("red","green"),
#             col=heat.colors(2),
            log="y",
            args.legend = list(x="topright"))
#   dev.off()
  
#   ?title
  title(main=tetext,cex=4)
  title(ylab="Liczba wystąpień",cex=3)
  title(xlab="Wartość wskazówki",cex=2)
#   legend(x="topright",cex=1.5,border="white", c("ten sam autor","różni autorzy") ,
#        col=c("red","green"));
  # Restore default clipping rect
#   par(mar=c(5, 4, 4, 2) + 0.1)        
  #filename = paste("barplot_0",i-1,"thClue.pdf", sep="")
  #pdf(filename)
  #barplot(tab,legend=T,beside=T,main=tetext)
#   dev.off()  
}
