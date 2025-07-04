const { VM } = require('vm2');
const { Writable } = require('stream');

function executeAndCompareJavaScript(code, expectedOutput) {
    try {

        let output = '';
        const outputStream = new Writable({
            write(chunk, encoding, callback) {
                output += chunk.toString();
                callback();
            }
        });

        const sandbox = {
            console: {
                log: (...args) => {
                    outputStream.write(args.join(' ') + '\n');
                }
            }
        };

        const vm = new VM({
            sandbox: sandbox,
            timeout: 1000
        });
        vm.run(code);

        return output.trim() === expectedOutput.trim();
    } catch (error) {
        return false;
    }
}
//para probarlo
const testCases = [
    { code: 'console.log("Hola, mundo!");', expected: 'Hola, mundo!', desc: 'Código simple' },
    { code: 'console.log(2 + 3);', expected: '5', desc: 'Suma aritmética' },
    { code: 'let x = 5; console.log(x * 2);', expected: '10', desc: 'Variable y operación' },
    { code: 'console.log("Test");', expected: 'Wrong', desc: 'Salida incorrecta' },
    { code: 'throw new Error("Error");', expected: 'n/a', desc: 'Error de ejecución' },
    { code: 'while(true) {}', expected: 'n/a', desc: 'Bucle infinito (timeout)' }
];

testCases.forEach((test, index) => {
    const result = executeAndCompareJavaScript(test.code, test.expected);
    console.log(`Prueba ${index + 1}: ${test.desc} - ${result ? 'ÉXITO' : 'FALLO'} (Código: '${test.code}', Esperado: '${test.expected}')`);
});