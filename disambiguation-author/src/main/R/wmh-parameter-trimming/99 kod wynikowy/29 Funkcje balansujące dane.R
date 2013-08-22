balanceDataAndReduceNumer <- function(f,fract){
  #podzielenie danych na zbiór TRUE i FALSE
  f_true <- f[ f$samePerson!=FALSE ,]
  summary(f_true)
  f_false <-f[ f$samePerson==FALSE ,]
  summary(f_false)

#   print("False ratio [%]")
#   100*nrow(f_false)/nrow(f)
#   print("True ratio [%]")
#   100*nrow(f_true)/nrow(f)
  
  #Wylosowanie ze zbioru TRUE takiej procentowej części 
  #jaką stanowi zbiór FALSE w zbiorze TRUE
  #===zbalansowanie danych
  rhv <- runif(nrow(f_true))
  f_true_sel <- f_true[rhv<nrow(f_false)/nrow(f_true),]
  nrow(f_false)/nrow(f_true_sel)
  
  # Wylosowanie ze zbiorów TRUE i FALSE co tysięcznej próbki
  rhv <- runif(nrow(f_true_sel))
  fs_t <- f_true_sel[rhv<fract,]
  summary(fs_t)
  
  rhv <- runif(nrow(f_false))
  fs_f <- f_false[rhv<fract,]
  
  f_new <- rbind(fs_t, fs_f)
  return(f_new)
}