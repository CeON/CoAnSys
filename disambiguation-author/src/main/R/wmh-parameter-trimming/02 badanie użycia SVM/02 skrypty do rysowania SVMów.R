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



# 2) Task: not-linear-separable data
#Prepare the trainingset (data,class).
data2 <- seq(1,10)
classes2 <- c('b','b','b','a','a','a','a','b','b','b')

#Build the model with a linear kernel.
model2 <- svm(data2,classes2,type='C',kernel='linear')

#Check if the model is able to classify your training data. If not: Why not?
predict(model2,data2)
table(predict(model2,data2), classes2)

#Try out the non-linear kernels (hint: ?svm), and check the performance . Does the choice of kernel matter?

#3) Task: Example from the manual (iris-data)
#Look in the manual and run the example on the classification of the iris data set. It is is possible to get more information about a model and its *attributes*?
#Get as many information as you can about the model!

#4) Task: Breast-cancer data

#In this task you should build a model able to discriminate benign ('gutartig') and  malignant ('boesartig') tumors, using morphological data. Your database consist of 699 correct classified observations.
#Download the data (http://www.potschi.de/svmtut/breast-cancer-wisconsin.data), and save it in the directory from where you started the R-term. 
#Read the data from the CSV-file.
bcdata <- read.csv('breast-cancer-wisconsin.data',head=TRUE)

#Take a look at the column titles. Which feature does *not* contain information useful for classification?
names(bcdata)

#Separate the data from the classification. The subset()-funtion allows you to (un)select the right columns..
databcall <- subset(bcdata,select=c(-Samplecodenumber,-Class))
classesbcall <- subset(bcdata,select=Class)

#Take a subset of your data, that you will use as training data.
databctrain <- databcall[1:400,]
classesbctrain <- classesbcall[1:400,]

#Take a subset of your data, that you will use as test data.
databctest <- databcall[401:699,]
classesbctest <- classesbcall[401:699,]

#Build the model.
model <- svm(databctrain, classesbctrain)

#Validate the model. Does it work?
pred <- predict(model, databctest)
table(pred,t(classesbctest))

#Improving the performance of your models
#Some kernels have so called hyperparameters. These parameters affect the performance of the kernel, and at last your classification!
#The e1071-package provides a tool that does a grid search (Whats that?) and reports an estimation for the parameters. Get familiar with tune(), and use it to improve your model!
tune(svm, train.x=databctrain, train.y=classesbctrain, validation.x=databctest, validation.y=classesbctest, ranges = list(gamma = 2^(-1:1), cost = 2^(2:4)), control = tune.control(sampling = "fix"))

#5) Task: Custom classification 
#Get some interesting data you want to classify. Hint: Search the WWW for "classification benchmarks". Be sure that you can get the data in a format thats easy to read (ideal: CSV with column titles). Check if the datasets are complete, if not: think about how to deal with that. Hint: common symbols for missing measurements are: '?','N','*'.  Please write down from where you got the data.  Build, test and tune a model until the classification performance satisfies you.  Save the R-commands you used in a file classification-datafilename.r (in the correct order ;)).  Report the results.
#If you give me the permission, I'll put your results on this tutorials homepage. Others may learn from your examples.


