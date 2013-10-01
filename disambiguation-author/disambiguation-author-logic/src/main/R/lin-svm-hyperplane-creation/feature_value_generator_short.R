#import giveHyperplane(x,y) function
source(paste(getwd(),"linear.R", sep="/"))

main <- function(pI){
  #install or use kernlib
  if (! "kernlab" %in% row.names(installed.packages()))
    install.packages('kernlab')
  require(kernlab)
  #read input <cXId,cYId,f1,f2,...,fN,same>
  tabela <- read.table(pI, header=F, quote="\"")
  #remove contribs Id tabela[c(1,2)]
  tabela <- tabela[-c(1,2)]
  #change last param name to "decision"
  names(tabela)[ncol(tabela)] <- "decision"
  #print(tabela) #debug line
  #calculate linear SVM model
  model <- ksvm(decision~., data=as.matrix(tabela), type="C-svc", scaled=FALSE,kernel = "vanilladot");
  #calculate separative hyperplane function and print it
  print(giveHyperplane(model,tabela)[1])
}

pathToInputFile <- "/home/pdendek/icm_dane/svmInput.csv"
main(pathToInputFile)