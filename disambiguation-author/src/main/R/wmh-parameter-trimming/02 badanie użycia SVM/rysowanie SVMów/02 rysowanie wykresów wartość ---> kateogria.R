val <- c(0.9478638, 0.7477657, 0.9742675, 0.9008372, 0.4873001, 0.5097587, 0.6476510, 0.4552577, 0.5578296, 0.5728478, 0.1927945, 0.2624068, 0.2732615)
length(val)
categ <- factor (rep (c("t0", "t12", "t24", "t72"), c(4,3,3,3)))

#Will return useless boxplot 
plot(categ, val)

#Will return sth actually useful
if (! "ggplot2" %in% row.names(installed.packages()))
  install.packages('')
  require(ggplot2)
qplot(categ, val)