source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/27 funkcje pomocnicze.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/28 wczytanie i upodządkowanie danych.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/29 Funkcje balansujące dane.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/30 Funckje modyfikujące NULLe.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/31 Funkcje modyfikujące dane.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/32 Funkcje uruchamiające budowę modelu.R",sep=""))

### PATTERNS FOR LOOPS ###
# cost_pattern = 10^(0:3)
# frac_pattern = 2/10^(3)
# model_pattern = c(-1:2,10:12,20:22)
# kernel_pattern = 0:2
# degree_pattern = 1:10
# gamma_pattern = 1/c(2,4)
# coefficient_pattern = seq(0,1,by=0.5)
# 
# frac=1/1000
# mod=1
# ker=1
# deg=2
# gam=1/1000
# coef=0.1


wmh_with_null <- function(frac_pattern,model_pattern,
                          kernel_pattern,degree_pattern,
                          gamma_pattern,coefficient_pattern,log,dane
                          ,cost_pattern
                          ){
  ### AUXILIAR VARIABLE ###
  genCounter = 0;
  results = list()
  for(frac in frac_pattern){    
    if(log=="any") print(cat(paste("LEVEL-1:FRACTION LOG [",date(),"] ",100*(which(frac_pattern==frac)-1)/length(frac_pattern),"%"," of fraction complited" , sep="")))
#     frac=0.01
    s0 = balanceDataAndReduceNumer(d,frac) 
    ## A null-value conversion is mainly done 
    ## during a data conversion process,
    ## so eventually it is better to skip this stage
 
    ## n=0 null2Zero(d)
    ## n=2 null2MinusAbsMax(d)
    ## s1 = convertNull(s1,2)
    #convertNull(s1,n)
    
    # -1 no change
    # 00 MinusBool-3-val
    # 01 MinusBool-lin-scal
    # 02 MinusBool-sigm-scal
    # 10 MinusBay-2-val
    # 11 MinusBay-lin-scal
    # 12 MinusBay-sigm-scal
    for(mod in model_pattern){
      if(log=="any")print(cat(paste("    LEVEL-2:MODEL LOG [",date(),"] ",100*(which(model_pattern==mod)-1)/length(model_pattern),"%"," of models complited" , sep="")))
      s1 = convertData(s0,mod)  
        ################################################
        # funckja budujące model
        # 0 lin
        # 1 pol
        ################################################    
      for(cost in cost_pattern){
        for(ker in kernel_pattern){
          if(log=="any")print(cat(paste("        LEVEL-3:KERNEL LOG    [[[","frac=",frac," mod=",mod," ker=",ker,"]]]    [[[",date(),"]]]    ",100*(which(kernel_pattern==ker)-1)/length(kernel_pattern),"%"," of kernel complited" , sep="")))
          if(ker==0){
            deg=NA
            gam=NA
            coef=NA
            genCounter=genCounter+1;

            resu <- tryCatch(createModel(s1,ker,deg,gam, coef,cost),
                                   warning = function(w) w,
                                   error = function(w) w)
                  if(is(resu,"warning")){
                    if(log=="war")print(paste("            Caught warning:", conditionMessage( resu )))
                    lst <- list( "min-correct-rate%"=NA,
                                "avg-correct-rate%"=NA,
                                "max-correct-rate%"=NA,
                                "warning/error"=paste("Caught warning:", conditionMessage( resu )),"time.elapsed"=NA,"cost"=cost)
                    results[[genCounter]]=data.frame(frac=frac,mod=mod,
                                      ker=ker,deg=deg,gam=gam,coef=coef,lst)
                  }else if(is(resu,"error")){
                    if(log=="err")print(paste("            Caught error:", conditionMessage( resu )))
                    lst <- list( "min-correct-rate%"=NA,
                                "avg-correct-rate%"=NA,
                                "max-correct-rate%"=NA,
                                "warning/error"=paste("Caught error:", conditionMessage( resu )),"time.elapsed"=NA,"cost"=cost)
                    results[[genCounter]]=data.frame(frac=frac,mod=mod,
                                      ker=ker,deg=deg,gam=gam,coef=coef,lst)
                  }else{
                    results[[genCounter]]=data.frame(frac=frac,mod=mod,
                                      ker=ker,deg=deg,gam=gam,coef=coef,resu,"cost"=cost)
                  }
          }else{
            for(deg in degree_pattern){
              for(gam in gamma_pattern){
                for(coef in coefficient_pattern){                
                  genCounter=genCounter+1;
                  if(log=="any")print(cat(paste("            LEVEL-4:NON-LIN KERNEL LOG    [[[deg=",deg,",gam=",gam,",coef=",coef,"]]]    [[[",date(),"]]]    ",sep="")))
                  resu <- tryCatch(createModel(s1,ker,deg,gam, coef,cost),
                                   warning = function(w) w,
                                   error = function(w) w)
                  if(is(resu,"warning")){
                    if(log=="war")print(paste("            Caught warning:", conditionMessage( resu )))
                    lst <- list( "min-correct-rate%"=NA,
                                "avg-correct-rate%"=NA,
                                "max-correct-rate%"=NA,
                                "warning/error"=paste("Caught warning:", conditionMessage( resu )),"time.elapsed"=NA,"cost"=cost)
                    results[[genCounter]]=data.frame(frac=frac,mod=mod,
                                      ker=ker,deg=deg,gam=gam,coef=coef,lst)
                  }else if(is(resu,"error")){
                    if(log=="err")print(paste("            Caught error:", conditionMessage( resu )))
                    lst = list( "min-correct-rate%"=NA,
                                "avg-correct-rate%"=NA,
                                "max-correct-rate%"=NA,
                                "warning/error"=paste("Caught error:", conditionMessage( resu )),"time.elapsed"=NA,"cost"=cost)
                    results[[genCounter]]=data.frame(frac=frac,mod=mod,
                                      ker=ker,deg=deg,gam=gam,coef=coef,lst)
                  }else{
                    results[[genCounter]]=data.frame(frac=frac,mod=mod,
                                      ker=ker,deg=deg,gam=gam,coef=coef,resu,"cost"=cost)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  final = do.call("rbind", results)
  return(final)
}