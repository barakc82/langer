del ..\SwingItCrmServer\src\main\webapp\static\*.* /q 
copy dist\swingItCrm\index.html ..\SwingItCrmServer\src\main\webapp /b/y
copy dist\swingItCrm\*.js ..\SwingItCrmServer\src\main\webapp\static /b/y
copy dist\swingItCrm\*.css ..\SwingItCrmServer\src\main\webapp\static /b/y
xcopy dist\swingItCrm\assets ..\SwingItCrmServer\src\main\webapp\static\assets /s /e /b /y