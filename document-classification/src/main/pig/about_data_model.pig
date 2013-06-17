A = load '/user/pdendek/workflows//coansys_dc-train/1368831085.67/2/1368831152.69//results//model_*' as (categ:chararray,t:int,f1:double);
B = filter A by categ is not null;   
D = group B by f1; 
E = foreach D generate group as f1, COUNT(B) as occ; 
F = order E by f1 desc; 
store E into 'modelF1Occ';


/**** f1>0.5 -- 179 *****/
A = load '/user/pdendek/workflows//coansys_dc-train/1368831085.67/2/1368831152.69//results//model_*' as (categ:chararray,t:int,f1:double);
B = filter A by categ is not null;   
D = filter B by f1>0.5; 
E = group D all;
F = foreach E generate COUNT(D);
dump F; -- 179

/**** f1>0.3 -- 905 *****/
A = load '/user/pdendek/workflows//coansys_dc-train/1368831085.67/2/1368831152.69//results//model_*' as (categ:chararray,t:int,f1:double);
B = filter A by categ is not null;   
D = filter B by f1>0.3; 
E = group D all;
F = foreach E generate COUNT(D);
dump F; -- 905

/**** f1>0.2 -- 2310 *****/
A = load '/user/pdendek/workflows//coansys_dc-train/1368831085.67/2/1368831152.69//results//model_*' as (categ:chararray,t:int,f1:double);
B = filter A by categ is not null;   
D = filter B by f1>0.2; 
E = group D all;
F = foreach E generate COUNT(D);
dump F; -- 2310

/**** f1>0.1 -- 5535 *****/
A = load '/user/pdendek/workflows//coansys_dc-train/1368831085.67/2/1368831152.69//results//model_*' as (categ:chararray,t:int,f1:double);
B = filter A by categ is not null;   
D = filter B by f1>0.1; 
E = group D all;
F = foreach E generate COUNT(D);
dump F; -- 

/**** f1>0.0 -- 7900 *****/
A = load '/user/pdendek/workflows//coansys_dc-train/1368831085.67/2/1368831152.69//results//model_*' as (categ:chararray,t:int,f1:double);
B = filter A by categ is not null;   
E = group B all;
F = foreach E generate COUNT(B);
dump F; --
