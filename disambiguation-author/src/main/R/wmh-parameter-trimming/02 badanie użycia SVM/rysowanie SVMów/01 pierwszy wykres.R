########################################################################################################
### tips get from: http://stackoverflow.com/questions/1142294/how-do-i-plot-a-classification-graph-of-a-svm-in-r
### tutorial from: http://planatscher.net/svmtut/svmtut.html
### help for plot.svm: http://rss.acs.unt.edu/Rdoc/library/e1071/html/plot.svm.html
########################################################################################################

if (! "e1071" %in% row.names(installed.packages()))
  install.packages('e1071')
library('e1071')

# 1) Task: linear separable data
#Prepare the trainingset (data,class)

data1 <- seq(1,10,by=2)
classes1 <- c('a','a','a','b','b')

#This testdata will be used for validation of the model.

test1 <- seq(1,10,by=2) + 1

#Look in the manual and try to find out more about svm(). Which parameters seem to be the most important to you? Build the model.
model1 <- svm(data1,classes1,type='C',kernel='linear')

#View the model.
print(model1)
summary(model1)

# Check if the model is able to classify your *training* data.
predict(model1,data1)
pred <- fitted(model1)

predict(model1, test1, decision.values = TRUE, probability = FALSE)

table(pred, classes1)



# Validate your dataset. Does the prediction meet your expectation?
predict(model1,data1)
fitted(model1)


# Try to find out, where class border was set by svm (hint: use predict() to do that).

#output over 0 means "a"
#output below 0 means "b"
model$decision.values 

#Let's plot the result
#We need to add fake data, which differs a little among them
d <- data.frame(val=data1,fake=c(1,0,0,0,0),dec=classes1)
model = svm(dec~val+fake,data=d)
plot(model,d)

t <- data.frame(val=test1, fake=c(0.001,0,0,0,0),dec=classes1)
plot(model,t)

# From the plot we can see that decision value is slightly above 6!