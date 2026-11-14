param(
    [ValidateSet('build', 'test', 'run')]
    [string]$Task = 'test',
    [string]$JdkHome = $env:JAVA_HOME
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
$java = if ($JdkHome) { Join-Path $JdkHome 'bin/java.exe' } else { 'java' }
$javac = if ($JdkHome) { Join-Path $JdkHome 'bin/javac.exe' } else { 'javac' }
Push-Location $projectRoot
try {
    # Fresh output prevents deleted classes from making a broken build appear successful.
    $buildRoot = Join-Path $projectRoot 'out'
    New-Item -ItemType Directory -Force -Path $buildRoot | Out-Null
    $output = Join-Path $buildRoot ('build-' + [guid]::NewGuid().ToString('N'))
    New-Item -ItemType Directory -Path $output | Out-Null
    $sources = @(Get-ChildItem src -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
    if ($Task -eq 'test') {
        $sources += @(Get-ChildItem tests -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
    }
    & $javac --release 17 -encoding UTF-8 -Xlint:all -d $output @sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
    if ($Task -eq 'test') {
        $testClasses = Get-ChildItem tests/LanguageLearningApp/tests -Filter '*Test.java'
        foreach ($testClass in $testClasses) {
            & $java '-Dfile.encoding=UTF-8' -cp $output ('LanguageLearningApp.tests.' + $testClass.BaseName)
            if ($LASTEXITCODE -ne 0) { throw ('Test failed: ' + $testClass.BaseName) }
        }
    } elseif ($Task -eq 'run') {
        & $java '-Dfile.encoding=UTF-8' -cp $output LanguageLearningApp.Main
        if ($LASTEXITCODE -ne 0) { throw 'Application failed.' }
    } else {
        Write-Output ('Compiled classes: ' + $output)
    }
} finally {
    Pop-Location
}
