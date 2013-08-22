


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
