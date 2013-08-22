# Set this to TRUE to generate an error, otherwise try Ctrl-C (or ESC)
generateError <- FALSE;

filename <- tempfile();

?tryCatch
}}}}}}}}}}}}}}}}}}}

# Example of script releasing resources after an error or an interrupt.
tryCatch({
  fh <- file(filename, open="w");
  for (kk in 1:10) {
    cat("Iteration ", kk, "\n", sep="");
    aValue <- kk;
    if (generateError)
      stop("Script stopped due to an error.");
    Sys.sleep(1);
  }
  aValue <- 0;
}, interrupt = function(ex) {
  cat("An interrupt was detected.\n");
  print(ex);
}, error = function(ex) {
  cat("An error was detected.\n");
  print(ex);
}, finally = {
  cat("Releasing resources...");
  if (isOpen(fh)) {
    close(fh);
    rm(fh);
    file.remove(filename);
  }
  cat("done\n");
}) # tryCatch()

if (file.exists(filename)) {
  cat("Failed to remove temporary file: ", filename, "\n", sep="");
} else {
  cat("Successfully removed temporary file: ", filename, "\n", sep="");
}

if (exists("aValue")) {
  cat("Variable 'aValue' exists: ", aValue, "\n", sep="");
} else {
  cat("Variable 'aValue' does not exist.\n", sep="");
}