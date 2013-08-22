# read.csv(file, header = FALSE, sep = "", quote = "\"'",
#           dec = ".", row.names, col.names,
#           as.is = !stringsAsFactors,
#           na.strings = "NA", colClasses = NA, nrows = -1,
#           skip = 0, check.names = TRUE, fill = !blank.lines.skip,
#           strip.white = FALSE, blank.lines.skip = TRUE,
#           comment.char = "#",
#           allowEscapes = FALSE, flush = FALSE,
#           stringsAsFactors = default.stringsAsFactors(),
#           fileEncoding = "", encoding = "unknown", text)


file <- read.csv(file="/home/pdendek/WMH/00 badania/01 import wskazówek do CSV/and_9135418604463356.csv",header=T, quote="\"",sep=" ")
View(file)
#Order data by "id"
f2 <- file[with(file, order(id)), ]
View(f2)
#Delete additional column f2$row.names is not needed,
#it is only phantom column, not seen in the data frame

#Cast -1 to NA
#changing -1 to NA
fna <- f2
max <- 
for(i in 2:(ncol(f2)-1)){
   fna = fna[fna[,i]==-1,i]=NA
}
head(fna)
summary(fna)

#Usuwamy niepotrzebne wskazówki (w pełni nullowe, bądź bardzo nullowe)
f3 <-f2
f3$f0 <- NULL
f3$f1 <- NULL
f3$f5 <- NULL
f3$f6 <- NULL

#Sprawdzamy czy są jakieś wartości błędne, np. dla roku różnica więsza niż 1000lat, 1999, bo to znaczy, że jeden rok był błędnie zerem
tmp <- f3[,]
tmp
f3 <- f3[   f3$f7<1000    , ]
summary(f3)

# Pozostałe w grze wskazówki
#3CoContribution      	
#4CoClassif
#5CoKeywordPhrase
#8Year

f <- d[,-1]
# summary(f)

f_true <- f[ f$samePerson!=FALSE ,]
summary(f_true)
f_false <-f[ f$samePerson==FALSE ,]
summary(f_false)

print("False ratio [%]")
100*nrow(f_false)/nrow(f)
100*nrow(f_true)/nrow(f)

rhv <- runif(nrow(f_true))
f_true_sel <- f_true[rhv<nrow(f_false)/nrow(f_true),]
nrow(f_false)/nrow(f_true_sel)

rhv <- runif(nrow(f_true_sel))
fs_t <- f_true_sel[rhv<0.001,]
summary(fs_t)

rhv <- runif(nrow(f_false))
fs_f <- f_false[rhv<0.001,]
summary(fs_f)


f_new <- rbind(fs_t, fs_f)
head(f_new)
d <- f_new 

