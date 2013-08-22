source(paste(getwd(),"/R_AND/linear.R", sep=""))

x1 = seq(1,6,by=1)
x2 = runif(6)/1000
y = c(rep(-1,3),rep(1,3))
d <- data.frame(x1=x1
                ,x2=x2
                ,y=y)
head(d)
model <- ksvm(y~., data=as.matrix(d)
             , type="C-svc"
             , scaled=FALSE
              ,kernel = "vanilladot"
             )

giveHyperplane(model)[1]
plot(model, data=d)






plot(qnorm) # default range c(0, 1) is appropriate here,
            # but end values are -/+Inf and so are omitted.
plot(qlogis, main = "The Inverse Logit : qlogis()")
abline(h = 0, v = 0:2/2, lty = 3, col = "gray")

curve(sin, -2*pi, 2*pi, xname = "t")
curve(tan, xname = "t", add = NA,
      main = "curve(tan)  --> same x-scale as previous plot")

op <- par(mfrow = c(2, 2))
curve(x^3 - 3*x, -2, 2)
curve(x^2 - 2, add = TRUE, col = "violet")

## simple and advanced versions, quite similar:
plot(cos, -pi,  3*pi)
curve(cos, xlim = c(-pi, 3*pi), n = 1001, col = "blue", add = TRUE)

chippy <- function(x) sin(cos(x)*exp(-x/2))
curve(chippy, -8, 7, n = 2001)
plot (chippy, -8, -5)

for(ll in c("", "x", "y", "xy"))
   curve(log(1+x), 1, 100, log = ll,
         sub = paste("log= '", ll, "'", sep = ""))
par(op)









