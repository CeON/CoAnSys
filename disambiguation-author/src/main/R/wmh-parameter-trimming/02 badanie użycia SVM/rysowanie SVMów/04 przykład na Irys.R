########################################################################################################
### tips get from: http://stackoverflow.com/questions/1142294/how-do-i-plot-a-classification-graph-of-a-svm-in-r
### tutorial from: http://planatscher.net/svmtut/svmtut.html
### help for plot.svm: http://rss.acs.unt.edu/Rdoc/library/e1071/html/plot.svm.html
### tutorial for interesting plots: http://www.harding.edu/fmccown/r/#autosdatafile
### learning hash tables in R: http://userprimary.net/posts/2010/05/29/rdict-skip-list-hash-table-for-R/
########################################################################################################

if (! "e1071" %in% row.names(installed.packages()))
  install.packages('e1071')
library('e1071')

if (! "ggplot2" %in% row.names(installed.packages()))
  install.packages('')
  require(ggplot2)

?svm
#3) Task: Example from the manual (iris-data)
#Look in the manual and run the example on the classification of the iris data set. It is is possible to get more information about a model and its *attributes*?
#Get as many information as you can about the model!

################################################################
########################### Examples ###########################
################################################################

data(iris)
attach(iris)

head(iris)


## classification mode
# default with factor response:
model <- svm(iris$Species ~., data = iris)
model

print(model)
summary(model)

plot(cmdscale(dist(iris[,-5])),
     col = as.integer(iris[,5]),
     pch = c("o","+")[1:150 %in% model$index + 1])


# alternatively the traditional interface:
x <- subset(iris, select = -Species)
y <- Species
model <- svm(x, y) 

print(model)
summary(model)

# test with train data
pred <- predict(model, x)
# (same as:)
pred <- fitted(model)

# Check accuracy:
table(pred, y)

# compute decision values and probabilities:
pred <- predict(model, x, decision.values = TRUE)
attr(pred, "decision.values")[1:4,]

# visualize (classes by color, SV by crosses):
plot(cmdscale(dist(iris[,-5])),
     col = as.integer(iris[,5]),
     pch = c("o","+")[1:150 %in% model$index + 1])

## try regression mode on two dimensions

# create data
x <- seq(0.1, 5, by = 0.05)
y <- log(x) + rnorm(x, sd = 0.2)

# estimate model and predict input values
m   <- svm(x, y)
new <- predict(m, x)

# visualize
plot(x, y)
points(x, log(x), col = 2)
points(x, new, col = 4)

## density-estimation

# create 2-dim. normal with rho=0:
X <- data.frame(a = rnorm(1000), b = rnorm(1000))
attach(X)

# traditional way:
m <- svm(X, gamma = 0.1)

# formula interface:
m <- svm(~., data = X, gamma = 0.1)

head(X)

# or:
m <- svm(~ a + b, gamma = 0.1)

# test:
newdata <- data.frame(a = c(0, 4), b = c(0, 4))
predict (m, newdata)

# visualize:
plot(X, col = 1:1000 %in% m$index + 1, xlim = c(-5,5), ylim=c(-5,5))
points(newdata, pch = "+", col = 2, cex = 5)

# weights: (example not particularly sensible)
i2 <- iris
levels(i2$Species)[3] <- "versicolor"
summary(i2$Species)
wts <- 100 / table(i2$Species)
wts
m <- svm(Species ~ ., data = i2, class.weights = wts)


## more than two variables: fix 2 dimensions
data(iris)
m2 <- svm(Species~., data = i2, class.weights = wts)
plot(m2, iris, Petal.Width ~ Petal.Length,
     slice = list(Sepal.Width = 2, Sepal.Length = 3))

## plot with custom symbols and colors
plot(m, cats, svSymbol = 1, dataSymbol = 2, symbolPalette = rainbow(4),
color.palette = terrain.colors)

plot(cmdscale(dist(iris[,-5])),
     col = as.integer(iris[,5]),
     pch = c("o","+")[1:150 %in% model$index + 1])
