pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R"),sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

dane <- d

head(dane)

##############################################
######## Pozostałe w grze wskazówki ##########
##############################################

####################################
########### 3CoContribution ########
########### 4CoClassif #############
########### 5CoKeywordPhrase #######
########### 8Year ##################
####################################

####################################
########### ALL DATA ###############
AllDataRaport <- function(){
print(paste("Generally SAME PERSON observations take",100*nrow(dane[dane$samePerson==TRUE,])/nrow(dane),"%"))
print(paste("CoContribs null vall takes",100*nrow(dane[dane$f2==-1,])/nrow(dane),"%",
            "from which",100*nrow(dane[dane$f2==-1 & dane$samePerson==TRUE,])/nrow(dane[dane$f2==-1,]),"are SAME PERSONS"))
print(paste("CoClassif null vall takes",100*nrow(dane[dane$f3==-1,])/nrow(dane),"%",
            "from which",100*nrow(dane[dane$f3==-1 & dane$samePerson==TRUE,])/nrow(dane[dane$f3==-1,]),"are SAME PERSONS"))
print(paste("CoKeywordPhrases null vall takes",100*nrow(dane[dane$f4==-1,])/nrow(dane),"%",
            "from which",100*nrow(dane[dane$f4==-1 & dane$samePerson==TRUE,])/nrow(dane[dane$f4==-1,]),"are SAME PERSONS"))
print(paste("CoYear null vall takes",100*nrow(dane[dane$f7==-1,])/nrow(dane),"%",
            "from which",100*nrow(dane[dane$f7==-1 & dane$samePerson==TRUE,])/nrow(dane[dane$f7==-1,]),"are SAME PERSONS"))  
}
AllDataRaport()

summary(dane$f7)

#####################################
###### NULL PAIRS/GROUPS ############
print(paste("CoContribs null vall takes",100*nrow(dane[dane$f2==-1,])/nrow(dane),"%"))
print(paste("CoClassif null vall takes",100*nrow(dane[dane$f3==-1,])/nrow(dane),"%"))
print(paste("CoKeywordPhrases null vall takes",100*nrow(dane[dane$f4==-1,])/nrow(dane),"%"))
print(paste("CoYear null vall takes",100*nrow(dane[dane$f7==-1,])/nrow(dane),"%"))
#####################################
########### ALL EXAMPLES ############
print(nrow(dane))
#####################################
########### ZERO NULLS ###############
print(paste("Zero nulls are",
            nrow(dane[dane[,1]!=-1 
                                  & dane[,2]!=-1 
                                  & dane[,3]!=-1 
                                  & dane[,4]!=-1,   ])
            )
      )
#####################################
########### ALL NULLS ###############
print(paste("All nulls are",
            nrow(dane[dane[,1]==-1 
                                  & dane[,2]==-1 
                                  & dane[,3]==-1 
                                  & dane[,4]==-1,   ])
            )
      )
#####################################
########## THREE NULLS ##############
all3 <- 0
for(i in 1:4){
  ran <- 1:4
  ran <- ran[-i]
  
  da <- dane
  for(j in ran){
    da <- da[da[,j]==-1,]
  }
  all3 <- all3 + nrow(da)
}
print(paste("There are",all3,"null values, which takes",100*all3/nrow(dane)))
#####################################
########### ONE NULLS ###############
print(paste("One null are",
            nrow(dane[dane[,1]==-1 
                                  | dane[,2]==-1 
                                  | dane[,3]==-1 
                                  | dane[,4]==-1,   ])
            )
      )
#####################################
########### TWO NULLS ###############
all2 <- 0
for(i in 1:3){
  for(j in (i+1):4){
    all2 = all2 + nrow(dane[dane[,i]==-1 & dane[,j]==-1,])
    print(paste(i,j,all2))
  }
}
print(all2-2*3762)

#####################################
########## FOUR NULLS ###############
